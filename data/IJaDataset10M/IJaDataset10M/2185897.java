package jpiv2;

/** A configurable file chooser dialog.
 *
 */
public class FlexFileChooser extends javax.swing.JFileChooser {

    /** Constructor. */
    public FlexFileChooser() {
        java.awt.Dimension dimension = new java.awt.Dimension(600, 400);
        this.setPreferredSize(dimension);
    }

    /** Set the type of file you want to select.
     * Possible filetype ID's are: <br>
     * DIR = directories  <br>
     * JVC = JPIV vector data (UTF-8 space delimited table, may or may not have a Tecplot compatible header) <br>
     * JSC = JPIV executable script (UTF-8 plain text) <br>
     * VEC = PIVware vector data (like JVC, no header) <br>
     * VEC_READ = All read compatible vector file formats <br>
     * DAT = Tecplot vector data (like JVC, with header). <br>
     * NC = netCDF PIV data (network common data form) <br>
     * JPF = JPIV profile data (UTF-8 space delimited table) <br>
     * JSC = JPIV executable script (UTF-8 plain text) <br>
     * PNG = Portable Network Graphic (lossless compressed image) <br>
     * IMX = DaVis compressed image format <br>
     * IM7 = DaVis 7 compressed image format <br>
     * PGM = pgm - Portable Grey Map (highly compatible format) <br>
     * TIF = Tagged Image File Format (float data support) <br>
     * ALL = all files <br>
     * IMG_WRITE = all image filetypes that can be written <br>
     * GRA_WRITE = all vector graphics formats that can be written <br>
     *
     * @param ID Filetype ID.
     * @param multiSelection Set whether multiple files can be selected or not.
     */
    public void setFiletype(int ID, boolean multiSelection) {
        this.setMultiSelectionEnabled(multiSelection);
        this.setFileSelectionMode(this.FILES_ONLY);
        removeAllFileFilters();
        addFileFilter(ID);
    }

    /** Get the currently selected filetype.
     * @return A string describing the selected filtype. This string is
     * identical with the first three characters of the filetype description of
     * the drop down list of filetypes in the dialog (e.g. 'png').
     */
    public String getSelectedFiletype() {
        jpiv2.JpivFileFilter ff = (jpiv2.JpivFileFilter) this.getFileFilter();
        String dc = ff.getDescription();
        return (dc.substring(0, dc.indexOf(" ")));
    }

    private void addFileFilter(int ID) {
        switch(ID) {
            case DIR:
                {
                    this.setFileSelectionMode(this.DIRECTORIES_ONLY);
                    break;
                }
            case JVC:
                {
                    this.addChoosableFileFilter(ffjvc);
                    break;
                }
            case JSC:
                {
                    this.addChoosableFileFilter(ffjsc);
                    break;
                }
            case PNG:
                {
                    this.addChoosableFileFilter(ffpng);
                    break;
                }
            case IMX:
                {
                    this.addChoosableFileFilter(ffimx);
                    break;
                }
            case IM7:
                {
                    this.addChoosableFileFilter(ffim7);
                    break;
                }
            case PGM:
                {
                    this.addChoosableFileFilter(ffpgm);
                    break;
                }
            case TIF:
                {
                    this.addChoosableFileFilter(fftif);
                    break;
                }
            case DAT:
                {
                    this.addChoosableFileFilter(ffdat);
                    break;
                }
            case VEC:
                {
                    this.addChoosableFileFilter(ffvec);
                    break;
                }
            case NC:
                {
                    this.addChoosableFileFilter(ffnc);
                    break;
                }
            case VEC_READ:
                {
                    this.addVecReadFileFilters();
                    break;
                }
            case ALL:
                {
                    this.addAllFileFilters();
                    break;
                }
            case IMG_WRITE:
                {
                    this.addImgWriteFileFilters();
                    break;
                }
            case IMG_READ:
                {
                    this.addImgReadFileFilters();
                    break;
                }
            case GRA_WRITE:
                {
                    this.addGraWriteFileFilters();
                    break;
                }
        }
    }

    private void addAllFileFilters() {
        this.addChoosableFileFilter(ffjvc);
        this.addChoosableFileFilter(ffdat);
        this.addChoosableFileFilter(ffjsc);
        this.addChoosableFileFilter(ffpng);
        this.addChoosableFileFilter(ffimx);
        this.addChoosableFileFilter(ffim7);
        this.addChoosableFileFilter(ffpgm);
        this.addChoosableFileFilter(fftif);
        this.addChoosableFileFilter(ffdat);
        this.addChoosableFileFilter(ffvec);
        this.addChoosableFileFilter(ffnc);
        this.addChoosableFileFilter(ffimgRead);
        this.addChoosableFileFilter(ffimgWrite);
        this.addChoosableFileFilter(ffgraWrite);
        this.addChoosableFileFilter(ffvecRead);
        this.setAcceptAllFileFilterUsed(true);
    }

    private void addImgWriteFileFilters() {
        this.addChoosableFileFilter(ffpgm);
        this.addChoosableFileFilter(fftif);
        this.addChoosableFileFilter(ffpng);
        this.setAcceptAllFileFilterUsed(false);
    }

    private void addImgReadFileFilters() {
        this.addChoosableFileFilter(ffpng);
        this.addChoosableFileFilter(ffim7);
        this.addChoosableFileFilter(ffimx);
        this.addChoosableFileFilter(ffpgm);
        this.addChoosableFileFilter(fftif);
        this.addChoosableFileFilter(ffimgRead);
        this.setAcceptAllFileFilterUsed(false);
    }

    private void addVecReadFileFilters() {
        this.addChoosableFileFilter(ffjvc);
        this.addChoosableFileFilter(ffdat);
        this.addChoosableFileFilter(ffvec);
        this.addChoosableFileFilter(ffnc);
        this.addChoosableFileFilter(ffvecRead);
        this.setAcceptAllFileFilterUsed(false);
    }

    private void addGraWriteFileFilters() {
        this.addChoosableFileFilter(ffswf);
        this.addChoosableFileFilter(ffemf);
        this.addChoosableFileFilter(ffsvg);
        this.addChoosableFileFilter(ffpdf);
        this.addChoosableFileFilter(ffeps);
        this.setAcceptAllFileFilterUsed(false);
    }

    private void removeAllFileFilters() {
        this.removeChoosableFileFilter(ffjvc);
        this.removeChoosableFileFilter(ffjsc);
        this.removeChoosableFileFilter(ffpng);
        this.removeChoosableFileFilter(ffimx);
        this.removeChoosableFileFilter(ffim7);
        this.removeChoosableFileFilter(ffpgm);
        this.removeChoosableFileFilter(fftif);
        this.removeChoosableFileFilter(ffdat);
        this.removeChoosableFileFilter(ffvec);
        this.removeChoosableFileFilter(ffnc);
        this.removeChoosableFileFilter(ffimgRead);
        this.removeChoosableFileFilter(ffimgWrite);
        this.removeChoosableFileFilter(ffvecRead);
        this.removeChoosableFileFilter(ffgraWrite);
    }

    /**
     * Select only directories.
     */
    public static final int DIR = 0;

    /**
     * Show only jpiv vector files.
     */
    public static final int JVC = 1;

    /**
     * Show only jpiv script files.
     */
    public static final int JSC = 2;

    /**
     * Show only portable network graphic files.
     */
    public static final int PNG = 3;

    /**
     * Show only LaVision imx image files.
     */
    public static final int IMX = 4;

    /**
     * Show only LaVision im7 image files.
     */
    public static final int IM7 = 5;

    /**
     * Show only portable grey map image files.
     */
    public static final int PGM = 6;

    /**
     * Show only tagged image files.
     */
    public static final int TIF = 7;

    /**
     * Show only Tecplot data files.
     */
    public static final int DAT = 8;

    /**
     * Show only PivWare vector files.
     */
    public static final int VEC = 9;

    /**
     * Show only network common data form files.
     */
    public static final int NC = 10;

    /**
    * Show only encapsulated post script files.
    */
    public static final int EPS = 11;

    /**
    * Show only portable data format files.
    */
    public static final int PDF = 12;

    /**
    * Show only enhanced meta format files.
    */
    public static final int EMF = 13;

    /**
    * Show only scalable vector graphics files.
    */
    public static final int SVG = 14;

    /**
    * Show only shock wave format files.
    */
    public static final int SWF = 15;

    /**
     * Show all files.
     */
    public static final int ALL = 100;

    /**
     * Show all write compatible image files.
     */
    public static final int IMG_WRITE = 200;

    /**
     * Show all read compatible image files.
     */
    public static final int IMG_READ = 300;

    /**
     * Show all read compatible vector files.
     */
    public static final int VEC_READ = 400;

    /**
     * Show all write compatible vector graphics formats.
     */
    public static final int GRA_WRITE = 500;

    private String[] jvc = { "jvc" };

    private String[] jsc = { "jsc" };

    private String[] png = { "png" };

    private String[] tif = { "tif", "tiff" };

    private String[] pgm = { "pgm" };

    private String[] imx = { "imx" };

    private String[] im7 = { "im7" };

    private String[] dat = { "dat" };

    private String[] vec = { "vec" };

    private String[] nc = { "nc" };

    private String[] eps = { "eps" };

    private String[] pdf = { "pdf" };

    private String[] emf = { "emf" };

    private String[] svg = { "svg" };

    private String[] swf = { "swf" };

    private String[] imgWrite = { "png", "tif", "tiff", "pgm" };

    private String[] imgRead = { "png", "tif", "tiff", "pgm", "im7", "imx" };

    private String[] vecRead = { "jsc", "vec", "dat", "nc" };

    private String[] graWrite = { "eps", "pdf", "emf", "svg", "swf" };

    private jpiv2.JpivFileFilter ffjvc = new jpiv2.JpivFileFilter(jvc, "jvc - JPIV vector data (UTF-8 space delimited table)");

    private jpiv2.JpivFileFilter ffjsc = new jpiv2.JpivFileFilter(jsc, "jsc - JPIV executable script (UTF-8 plain text)");

    private jpiv2.JpivFileFilter ffpng = new jpiv2.JpivFileFilter(png, "png - Portable Network Graphic (lossless compressed image)");

    private jpiv2.JpivFileFilter fftif = new jpiv2.JpivFileFilter(tif, "tiff - Tagged Image File Format (float data support)");

    private jpiv2.JpivFileFilter ffpgm = new jpiv2.JpivFileFilter(pgm, "pgm - Portable Grey Map (highly compatible format)");

    private jpiv2.JpivFileFilter ffimx = new jpiv2.JpivFileFilter(imx, "imx - DaVis compressed image format");

    private jpiv2.JpivFileFilter ffim7 = new jpiv2.JpivFileFilter(im7, "im7 - DaVis 7 compressed image format");

    private jpiv2.JpivFileFilter ffdat = new jpiv2.JpivFileFilter(dat, "dat - Tecplot vector data");

    private jpiv2.JpivFileFilter ffvec = new jpiv2.JpivFileFilter(vec, "vec - PIVware vector data");

    private jpiv2.JpivFileFilter ffnc = new jpiv2.JpivFileFilter(nc, "nc - netCDF PIV data (network common data format)");

    private jpiv2.JpivFileFilter ffeps = new jpiv2.JpivFileFilter(eps, "eps - encapsulated post script");

    private jpiv2.JpivFileFilter ffpdf = new jpiv2.JpivFileFilter(pdf, "pdf - portable document format");

    private jpiv2.JpivFileFilter ffemf = new jpiv2.JpivFileFilter(emf, "emf - enhanced meta format");

    private jpiv2.JpivFileFilter ffsvg = new jpiv2.JpivFileFilter(svg, "svg - scalable vector graphics");

    private jpiv2.JpivFileFilter ffswf = new jpiv2.JpivFileFilter(swf, "swf - shock wave format");

    private jpiv2.JpivFileFilter ffimgWrite = new jpiv2.JpivFileFilter(imgWrite, "png, tif, tiff, pgm - write compatible image formats");

    private jpiv2.JpivFileFilter ffimgRead = new jpiv2.JpivFileFilter(imgRead, "png, tif, tiff, pgm, imx, im7 - read compatible image formats");

    private jpiv2.JpivFileFilter ffvecRead = new jpiv2.JpivFileFilter(vecRead, "jvc, vec, dat, nc - read compatible vector formats");

    private jpiv2.JpivFileFilter ffgraWrite = new jpiv2.JpivFileFilter(graWrite, "eps, pdf, emf, svg, swf - write compatible vector graphics formats");
}
