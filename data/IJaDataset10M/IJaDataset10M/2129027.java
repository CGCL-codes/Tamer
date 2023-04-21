package marcin.downloader.model;

import marcin.downloader.model.utils.Link;

public class Links {

    private static Links l = new Links();

    private static Link[] LINKS;

    private static String[] LINKSSTR = { "http://uploading1.com/x.part01.rar.html", "http://uploading1.com/x.part02.rar.html", "http://uploading1.com/x.part03.rar.html", "http://uploading2.com/x.part02.rar.html", "http://uploading2.com/x.part02.rar.html", "http://uploading2.com/x.part02.rar.html", "http://uploading3.com/x.part02.rar.html", "http://uploading3.com/x.part02.rar.html", "http://uploading3.com/x.part02.rar.html" };

    private Links() {
        super();
        try {
            LINKS = new Link[] { new Link("http://uploading1.com/x.part01.rar.html"), new Link("http://uploading1.com/x.part02.rar.html"), new Link("http://uploading1.com/x.part03.rar.html"), new Link("http://uploading2.com/x.part04.rar.html"), new Link("http://uploading2.com/x.part05.rar.html"), new Link("http://uploading2.com/x.part06.rar.html"), new Link("http://uploading3.com/x.part07.rar.html"), new Link("http://uploading3.com/x.part08.rar.html"), new Link("http://uploading3.com/x.part09.rar.html") };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final Link[] getLINKS() {
        return LINKS;
    }

    public static final String[] getLINKSSTR() {
        return LINKSSTR;
    }
}
