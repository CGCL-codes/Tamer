package eu.keep.uphec.help;

import java.net.URL;

/**
* The HelpSectionInfo class contains the information of a tree node  
* 
* @author Antonio Ciuffreda 
*/
public class HelpSectionInfo {

    private String title;

    private URL content;

    public HelpSectionInfo(String title, URL content) {
        this.title = title;
        this.content = content;
    }

    public URL getContent() {
        return content;
    }

    public String toString() {
        return title;
    }
}
