package easyJ.system.service;

public class TreeServiceFactory {

    public TreeServiceFactory() {
    }

    public static TreeService getTreeService() {
        return TreeServiceWholeHtmlImpl.getInstance();
    }
}
