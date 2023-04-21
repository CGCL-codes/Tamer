package org.opensecurepay.ui.spring.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.ApplicationLauncher;

/**
 * The OpenSecurePay GUI as a spring richclient application.
 * @author Christoph Bohl
 */
public class OpenSecurePayApp {

    private static final Log _logger = LogFactory.getLog(OpenSecurePayApp.class);

    /**
	 * Main routine for starting openSecurePay.
	 * @param args
	 */
    public static void main(String[] args) {
        _logger.info("OpenSecurePayApp starting up");
        String rootContextDirectoryClassPath = "/org/opensecurepay/spring/app/ctx";
        String startupContextPath = rootContextDirectoryClassPath + "/osp-startup-context.xml";
        String richclientApplicationContextPath = rootContextDirectoryClassPath + "/osp-application-context.xml";
        try {
            new ApplicationLauncher(startupContextPath, new String[] { richclientApplicationContextPath });
        } catch (RuntimeException e) {
            _logger.error("RuntimeException during startup", e);
        }
    }
}
