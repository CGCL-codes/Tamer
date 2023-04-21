package newtonERP.viewers.firstStep;

import newtonERP.common.ActionLink;
import newtonERP.module.generalEntity.ImageFile;
import newtonERP.module.generalEntity.SplashScreen;
import newtonERP.viewers.secondStep.ImageFileViewer;
import newtonERP.viewers.secondStep.LinkViewer;

/**
 * Sert à afficher des splash screens
 * 
 * @author Guillaume Lacasse
 */
public class SplashScreenViewer {

    /**
	 * @param entity splash screen à voir
	 * @return html du splash screen
	 */
    public static String getHtmlCode(SplashScreen entity) {
        String htmlCode = "";
        for (ImageFile imageFile : entity.getImageFileList()) {
            htmlCode += ImageFileViewer.getHtmlCode(imageFile);
        }
        htmlCode += "<ul>";
        for (ActionLink link : entity.getActionLinkList()) {
            String currentLinkHtml = LinkViewer.getHtmlCode(link);
            if (currentLinkHtml.length() > 0) {
                htmlCode += "<li>" + currentLinkHtml + "</li>";
            }
        }
        htmlCode += "</ul>";
        return htmlCode;
    }
}
