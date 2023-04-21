package module;

import game.Game;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import utiities.Path;
import communication.ClientInfo;
import communication.Roles;
import content.BaseContent;
import content.ContentBag;
import content.ContentType;
import content.Image;

public class ContentModule extends DistributorModule<BaseContent<?>> {

    public static final ModuleType[] DEFAULTTYPES = { ModuleType.Content };

    private static String ContentPath = "./content/";

    private static String ImagePath = ContentPath + "/images/";

    private Hashtable<String, Integer> contentFileDic;

    public ContentModule(Game game, ComModule com) {
        super(game, com, DEFAULTTYPES, Roles.CONTENT_SERVER, Roles.DISTRIBUTOR);
        contentFileDic = new Hashtable<String, Integer>();
        start();
    }

    public static void SetContentPath(String setPath) {
        ContentPath = setPath;
        ImagePath = ContentPath + "/images/";
    }

    protected void print(String p) {
        super.print("Content Module> " + p);
    }

    protected void printerr(String p) {
        super.printerr("Content Module> " + p);
    }

    protected void printwarn(String p) {
        super.printwarn("Content Module> " + p);
    }

    public void CreateNewImage(String filePath) {
        BaseContent<?> img = new Image(ImagePath + filePath);
        int id = createNewObject(img);
        String fileName = Path.GetFilename(filePath, true);
        contentFileDic.put(fileName, id);
    }

    public int GetContentID(String fileName) {
        if (contentFileDic.containsKey(fileName) == false) {
            printerr("The content file does not exist!: " + fileName);
            return -1;
        }
        return contentFileDic.get(fileName);
    }

    public ContentBag<BufferedImage> GetContentImage(String fileName) {
        int index = GetContentID(fileName);
        ContentBag<BufferedImage> contentBag;
        contentBag = new ContentBag<BufferedImage>(this, fileName, index, ContentType.Image);
        return contentBag;
    }

    public Object GetDefault(ContentType type) {
        switch(type) {
            case Image:
                return new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        }
        return null;
    }

    public BaseContent<?> getObject(int id) {
        BaseContent<?> content = super.getObject(id);
        if (content != null) return content;
        requestObject(id);
        return null;
    }

    public void clientLost(ClientInfo cinfos) {
    }
}
