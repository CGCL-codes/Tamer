package org.one.stone.soup.wiki.renderers;

import org.one.stone.soup.authentication.server.Login;
import org.one.stone.soup.wiki.controller.WikiControllerInterface;
import org.one.stone.soup.wiki.processor.ExternalResourceHelper;
import org.one.stone.soup.wiki.processor.FileHelper;
import org.one.stone.soup.wiki.processor.JavascriptEngine;
import org.one.stone.soup.wiki.processor.JavascriptHelper;
import org.one.stone.soup.wiki.processor.WikiHelper;
import org.one.stone.soup.xml.XmlElement;
import org.one.stone.soup.xml.XmlParser;

public class WikiExtensionRenderer implements WikiTagRenderer {

    public void render(String pageName, StringBuffer target, StringBuffer data, int count, WikiControllerInterface builder, WikiLinkParser linkRenderer) {
        try {
            XmlParser parser = new XmlParser();
            XmlElement element = parser.parseElement("<" + data.toString().replace('\'', '\"') + "/>");
            Login ownerLogin = builder.getFileManager().getLoginForPageAuthor(pageName, builder.getSystemLogin());
            ownerLogin.setLoggedIn(true);
            String script = builder.getFileManager().getPageAttachmentAsString("/OpenForum/Extensions/" + element.getName(), "renderer.sjs", ownerLogin);
            if (script != null) {
                JavascriptEngine js = new JavascriptEngine();
                JavascriptHelper jsHelper = new JavascriptHelper("/OpenForum/Extensions/" + element.getName() + "/renderer.sjs", js, builder, builder.getCatalogue(), builder.getFileManager(), ownerLogin);
                WikiHelper wikiHelper = new WikiHelper(builder, ownerLogin);
                FileHelper fileHelper = new FileHelper(builder, ownerLogin);
                ExternalResourceHelper externalHelper = new ExternalResourceHelper(builder.getFileManager(), ownerLogin);
                js.register(jsHelper, "js");
                js.register(wikiHelper, "wiki");
                js.register(fileHelper, "file");
                js.register(pageName, "pageName");
                js.register(element, "extension");
                js.register(externalHelper, "external");
                script = "function renderSJS() {" + script + "} renderSJS();";
                target.append(js.runJavascript("/OpenForum/Extensions/" + element.getName() + "/renderer.sjs", script));
            }
        } catch (Throwable e) {
            target.append("Error parsing Extension tag [{" + data + "}]. Exception:" + e);
        }
    }
}
