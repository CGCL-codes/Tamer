package edu.whitman.halfway.jigs;

import edu.whitman.halfway.util.*;
import java.util.*;
import java.io.*;
import java.awt.Dimension;
import org.apache.log4j.Logger;

/** 
    Uses a directory containing a picture index file, an image type
    file, and possibly some different sized pictures, as a Picture
    Source.

  File Format:
 
  <pre>
  Picture:  "<keyName> <hashCode>"
  Image:  <ImageType.longName> "<fileName>" <width>  <height>
  Image:  <ImageType.longName> "<fileName>" <width>  <height>
  </pre>
    
*/
public class DirImageSource implements ImageSource {

    public static final String PICTURE_INDEX_FILE = "picture_index.txt";

    /** map from MediaKeys to List of BasicImage objects.        
        These are the raw, unbound Images.
    */
    Map<MediaKey, List<Image>> imageMap = new HashMap<MediaKey, List<Image>>(100);

    ImageTypeSource typeSource;

    File picSpecFile;

    /** where our images live */
    File picSourceDir;

    /** if this is true, then when we readFromFile we check that each */
    boolean cleanAndCheck = false;

    static Logger log = Logger.getLogger(DirImageSource.class);

    private boolean needToWrite = false;

    /** Creates an ImagePictureSource on the given directory.  

        If imageDir exists, we assumes the associated index files
        already exists, ie, this is a persisted ImageDirPictureSource.
        the set of image types we want in the directory is taken to be
        the union of the current set of types (from the types file)
        and any types passed in.

        If the directory does not exist, it will be created and
        treated as a new ImageDirPictureSoure directory.  TypeSource
        can't be null in this case.  
    */
    public DirImageSource(File imageDir) {
        log.debug("Constructor called on " + imageDir);
        this.picSourceDir = imageDir.getAbsoluteFile();
        picSpecFile = new File(imageDir, PICTURE_INDEX_FILE);
        if (imageDir == null) {
            throw new IllegalArgumentException("imageDir can't be null");
        }
        if (isValidDir(imageDir)) {
            if (log.isInfoEnabled()) log.info("Trying to initialize from existing directory " + imageDir);
            if (!imageDir.isDirectory()) {
                throw new JigsException("imageDir " + imageDir + " exists but isn't a directory");
            }
            if (!picSpecFile.exists()) {
                throw new JigsException("Picture Index File " + picSpecFile + " does not exist.");
            }
            readFromFile();
            if (log.isDebugEnabled()) log.debug("Created ImageDirPictureSource with " + size() + " pictures.");
        } else {
            log.info("Trying to create new directory.");
            if (!imageDir.exists() && !imageDir.mkdir()) {
                throw new IllegalArgumentException("Error, coudn't create directory " + imageDir);
            }
            if (!imageDir.isDirectory()) {
                throw new IllegalArgumentException("imageDir " + imageDir + " isn't a directory.");
            }
        }
    }

    public void setTypeSource(ImageTypeSource its) {
        this.typeSource = its;
    }

    public ImageTypeSource getTypeSource() {
        return this.typeSource;
    }

    public int size() {
        return imageMap.size();
    }

    /** Gives a good indication that the directory is a valid image
        directory.  But doesn't actually read index files, so might
        return a false positive */
    public static boolean isValidDir(File dir) {
        if (dir == null) {
            return false;
        }
        File pf = new File(dir, PICTURE_INDEX_FILE);
        return dir.isDirectory() && pf.isFile();
    }

    /**
       Returns image list by getting images from Default source,
       adding our images, creating reference images to original image,
       and binding images to current ImageTypeSource.

       XXX not currently lazy, possibly should be (but be careful of
       invalidation if ImageTypeSource changes.
    */
    public List getImages(MediaKey pd) {
        List<Image> list = ImageSourceProvider.getDefaultImageSource().getImages(pd);
        List<Image> localList = imageMap.get(pd);
        if (localList != null) {
            list.addAll(localList);
        } else {
            log.warn("No local pictures for " + pd);
        }
        if (typeSource != null) {
            log.debug("list before binding = " + list);
            log.debug("binding ...");
            bindImagesToTypes(list);
            Collections.sort(list);
            log.debug("making references to other images ...");
            list.addAll(makeRefImages(pd, list));
        } else {
            log.warn("Error not binding types, typeSource is null.");
        }
        log.debug("Returning image list " + list);
        return list;
    }

    public Image getImage(MediaKey pd, ImageType type) {
        Iterator iter = getImages(pd).iterator();
        while (iter.hasNext()) {
            Image img = (Image) iter.next();
            if (type.equals(img.getType())) return img;
        }
        return null;
    }

    /** ensures that the source has images for the given picture for
     * whatever types are in the type source currently assigned */
    public void ensureHasAllTypes(MediaKey pd) {
        if (typeSource == null) {
            throw new IllegalArgumentException();
        }
        List crntRawImages = (List) imageMap.get(pd);
        Collection unboundTypes;
        if (crntRawImages != null) {
            unboundTypes = bindImagesToTypes(crntRawImages);
        } else {
            log.info("No images for " + pd + ", so all types unbound");
            unboundTypes = typeSource.getTypeCollection();
        }
        log.debug("types without images = " + unboundTypes);
        log.debug("All images in map " + imageMap.get(pd));
        Iterator iter = unboundTypes.iterator();
        while (iter.hasNext()) {
            ImageType type = (ImageType) iter.next();
            if (type.isRescaledCopy()) {
                File f = pd.getFile();
                Dimension size = ImageUtil.getSize(f);
                if (size != null) {
                    BasicImage image = ((ImageType) type).createImage(pd, picSourceDir, getNameModString(pd), size, ImageSourceProvider.getRootSource());
                    if (image != null) addToLocalList(pd, image);
                    needToWrite = true;
                } else {
                    log.error("Got null size for image " + f);
                }
            }
        }
    }

    protected void addToLocalList(MediaKey key, Image image) {
        List list = (List) imageMap.get(key);
        if (list == null) list = new LinkedList();
        assert (image != null);
        if (list.contains(image)) {
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                Image img = (Image) iter.next();
            }
            throw new IllegalArgumentException("Shouldn't be adding an Image we already have.");
        }
        list.add(image);
        imageMap.put(key, list);
    }

    protected void addToLocalList(MediaKey key, List newList) {
        List list = (List) imageMap.get(key);
        if (list == null) list = new LinkedList();
        assert newList != null;
        if (MiscUtil.containsAny(list, newList)) {
            throw new IllegalArgumentException("Shouldn't be adding an Image we already have.");
        }
        list.addAll(newList);
        imageMap.put(key, list);
    }

    protected void finalize() {
        writeBuffer();
    }

    /**
       Returns a list of images that are reference types to other
       image files.
    */
    protected List makeRefImages(MediaKey pd, List imgList) {
        List refImages = new ArrayList();
        Image origImage = ImageSourceProvider.getDefaultImageSource().getImage(pd, ImageType.ORIGINAL);
        Iterator iter = typeSource.getTypeCollection().iterator();
        while (iter.hasNext()) {
            ImageType type = (ImageType) iter.next();
            if (type.isOriginalRef()) {
                Dimension newSize = type.newSize(origImage.getDimension());
                if (newSize != null) {
                    refImages.add(new BasicImage(origImage.getFile(), newSize, type));
                }
            } else if (type.isLocalRef()) {
                ListIterator lIter = imgList.listIterator();
                Dimension newSize = type.newSize(origImage.getDimension());
                if (newSize != null) {
                    Image myImage = null;
                    while (lIter.hasNext() && myImage == null) {
                        Image img = (Image) lIter.next();
                        Dimension d = img.getDimension();
                        if (d.getWidth() > newSize.getWidth() && d.getHeight() > newSize.getHeight()) {
                            myImage = img;
                        }
                    }
                    if (myImage == null) {
                        if (typeSource.hasOriginalRefs()) {
                            myImage = origImage;
                        } else {
                            throw new JigsException("Cannot find an image to link to for ImageType " + type);
                        }
                    }
                    refImages.add(new BasicImage(myImage.getFile(), newSize, type));
                }
            }
        }
        log.debug("Adding refImages " + refImages);
        return refImages;
    }

    /**
       Takes the raw list of images (currently stored in Map) and
       assigns images a type based on the current typeSource.  Only
       types that have isRescaledCopy are considered.

       @returns a List of isRescaledCopy types for which no image bound.
     */
    protected List bindImagesToTypes(List images) {
        if (images == null) {
            throw new IllegalArgumentException("Images can't be null.");
        }
        if (typeSource == null) {
            throw new IllegalArgumentException("typeSource can't be null.");
        }
        Collection types = typeSource.getTypeCollection();
        Image[] boundImage = new Image[types.size()];
        Iterator typeIter = types.iterator();
        int ti = 0;
        while (typeIter.hasNext()) {
            ImageType type = (ImageType) typeIter.next();
            Iterator imgIter = images.iterator();
            while (imgIter.hasNext()) {
                Image img = (Image) imgIter.next();
                assert (img != null) : "images = " + images;
                if (type.binds(img)) {
                    if (boundImage[ti] == null || PictureUtil.isLarger(img, boundImage[ti])) {
                        boundImage[ti] = img;
                    }
                }
            }
            ti++;
        }
        typeIter = types.iterator();
        ti = 0;
        List unboundTypes = new LinkedList();
        while (typeIter.hasNext()) {
            ImageType type = (ImageType) typeIter.next();
            if (boundImage[ti] != null) {
                boundImage[ti].setType(type);
                log.debug("bound image " + boundImage[ti] + " to type " + type);
                assert type.binds(boundImage[ti]);
                assert type.isRescaledCopy();
            } else {
                if (type.isRescaledCopy()) {
                    unboundTypes.add(type);
                }
            }
            ti++;
        }
        return unboundTypes;
    }

    /** Writes all index files */
    public void writeBuffer() {
        if (needToWrite) {
            try {
                writePictures();
            } catch (IOException e) {
                log.error("Writing of pictures failled.", e);
                throw new JigsException("Writing of pictures failled", e);
            }
            needToWrite = false;
        }
    }

    /**
       Takes a collection of BasicPictures and writes them to an
       output file.  */
    private void writePictures() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(picSpecFile));
        Iterator ilIter = imageMap.entrySet().iterator();
        log.info("Writing " + size() + " pictures to index file " + picSpecFile);
        while (ilIter.hasNext()) {
            Map.Entry entry = (Map.Entry) ilIter.next();
            List imageList = (List) entry.getValue();
            MediaKey key = (MediaKey) entry.getKey();
            assert picSpecFile.getParentFile().equals(picSourceDir);
            writer.write(picToString(key.getString(), imageList, picSourceDir));
            writer.write(StringUtil.newline);
        }
        writer.flush();
        writer.close();
    }

    private String picToString(String key, List imageList, File outputDir) {
        StringBuffer buf = new StringBuffer();
        buf.append("Picture:  \"" + key + "\" " + imageHashCode(new File(key)) + StringUtil.newline);
        Iterator iter = imageList.iterator();
        while (iter.hasNext()) {
            Image image = (Image) iter.next();
            assert image != null : "Error, null image";
            buf.append("\tImage:  ");
            File img = image.getFile();
            String pathName;
            if (FileUtil.isEqual(outputDir, img.getParentFile(), false)) {
                pathName = img.getName();
            } else {
                pathName = img.getAbsolutePath();
            }
            if (pathName.indexOf("\"") != -1) {
                throw new Error("Can't have a path that contains double-quotes: " + pathName);
            }
            buf.append("\"" + pathName + "\"");
            buf.append(" " + image.getDimension().getWidth());
            buf.append(" " + image.getDimension().getHeight());
            buf.append(StringUtil.newline);
        }
        return buf.toString();
    }

    private List<String> splitLine(String line, String sep) {
        List<String> list = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        boolean inquote = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\\') {
                sb.append(c);
                if (i < line.length() - 1) {
                    sb.append(line.charAt(++i));
                }
            } else if (!inquote && c == ' ') {
                if (sb.length() > 0) {
                    list.add(sb.toString());
                    sb = new StringBuilder();
                }
            } else if (c == '"') {
                inquote = !inquote;
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            list.add(sb.toString());
        }
        return list;
    }

    private void readFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(picSpecFile));
            String line;
            NEXT_LINE: while ((line = skipToToken(reader, "Picture:")) != null) {
                List<String> tokens = splitLine(line, " ");
                if (tokens.size() != 3 || !tokens.get(0).equals("Picture:")) {
                    log.error("Invalid line, expected \"Picture: <picture key (path)>\", but read \n" + line);
                    continue NEXT_LINE;
                }
                String pictureKey = tokens.get(1);
                int firstDex = ((pictureKey.charAt(0) == '"') ? 1 : 0);
                int lastDex = ((pictureKey.charAt(pictureKey.length() - 1) == '"') ? pictureKey.length() - 1 : pictureKey.length());
                pictureKey = pictureKey.substring(firstDex, lastDex);
                int imageHash = Integer.parseInt(tokens.get(2));
                List images = getImages(reader);
                if (images == null) {
                    log.warn("Failed to find one or more expected images for picture " + pictureKey);
                } else {
                    File keyFile = new File(pictureKey);
                    if (keyFile.exists() && imageHash == imageHashCode(keyFile)) {
                        addToLocalList(new MediaKey(keyFile), images);
                    } else {
                        deleteImageFiles(images);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            log.error("Could not load Picture Config file.", e);
        }
    }

    protected void deleteImageFiles(List images) {
        Iterator iter = images.iterator();
        while (iter.hasNext()) {
            deleteImageFile(((Image) iter.next()).getFile());
        }
    }

    protected void deleteImageFile(File imgFile) {
        File par = imgFile.getParentFile();
        if (par != null && par.equals(picSourceDir)) {
            try {
                imgFile.delete();
                System.out.println("Deleted " + imgFile);
                log.info("Deleted " + imgFile);
            } catch (SecurityException e) {
                log.error("Couldn't delete " + imgFile, e);
            }
        } else {
            log.warn("Probably done with " + imgFile + " but not deleting, parent doesn't match imageDir.");
        }
    }

    /** Skips blank lines and comment lines until it finds a line that
        begins with token.  Returns the first line that begins with
        token if found, or resets reader and returns null if a
        different token is found.*/
    private String skipToToken(BufferedReader reader, String goalToken) throws IOException {
        String line;
        NEXT_LINE: while (true) {
            reader.mark(4096);
            line = reader.readLine();
            if (line == null) {
                return null;
            }
            StringTokenizer st = new StringTokenizer(line);
            if (!st.hasMoreTokens()) {
                continue NEXT_LINE;
            }
            String token = st.nextToken();
            if (token.startsWith("#")) {
                continue NEXT_LINE;
            } else if (token.equals(goalToken)) {
                return line;
            } else {
                reader.reset();
                return null;
            }
        }
    }

    /** loads an image for each image line for the picture.  if some
     * file is not found, we return null and we pretend we don't have
     * the image */
    private List getImages(BufferedReader reader) throws IOException {
        LinkedList images = new LinkedList();
        String line;
        boolean imageNotFound = false;
        NEXT_LINE: while ((line = skipToToken(reader, "Image:")) != null) {
            List tokens = StringUtil.tokenizeWithQuotes(line);
            if (tokens.size() != 4 || !tokens.get(0).equals("Image:") || !(tokens.get(2) instanceof Double) || !(tokens.get(3) instanceof Double)) {
                log.error("Invalid line, expected \"  Image:  <fileName> <width>  <height>\", but read \n" + line);
                continue NEXT_LINE;
            }
            String fileName = (String) tokens.get(1);
            int width = (int) ((Double) tokens.get(2)).doubleValue();
            int height = (int) ((Double) tokens.get(3)).doubleValue();
            File f = FileUtil.getValidFile(picSpecFile.getParentFile(), fileName);
            if (f != null && f.exists()) {
                images.add(new BasicImage(f, new Dimension(width, height), null));
            } else {
                log.warn("Failled to find file with name " + fileName + StringUtil.newline + " in dir " + picSourceDir);
                imageNotFound = true;
            }
        }
        if (imageNotFound) {
            return null;
        } else {
            return images;
        }
    }

    public String toString() {
        return "ImageDirPictureSource[" + picSourceDir + "]";
    }

    private int imageHashCode(File f) {
        if (!f.exists()) {
            throw new IllegalArgumentException("File " + f + " does not exist");
        }
        long[] input = new long[3];
        try {
            String path = f.getCanonicalPath();
            input[0] = path.hashCode();
        } catch (Exception e) {
            log.error("couldn't get absolute path for hashCode");
            input[0] = f.hashCode();
        }
        try {
            input[1] = f.length();
        } catch (SecurityException e) {
            log.error("couldn't get file length for unique name creation.");
        }
        input[2] = f.lastModified();
        int msize = 99999;
        return (int) Math.abs(((31 * 31 * 31 * input[0]) % msize + (31 * 31 * input[1]) % msize + 31 * input[2] % msize) % msize);
    }

    private String getNameModString(MediaKey pic) {
        File f = pic.getFile();
        int hashCode;
        try {
            String path = f.getCanonicalPath();
            hashCode = path.hashCode();
        } catch (Exception e) {
            log.warn("couldn't get absolute path for hashCode");
            hashCode = f.hashCode();
        }
        long length = 0;
        try {
            length = f.length();
        } catch (SecurityException e) {
            log.warn("couldn't get file length for unique name creation.");
        }
        String s = (hashCode < 0) ? "n" : "p";
        return s + Math.abs(hashCode) + "_" + length;
    }
}
