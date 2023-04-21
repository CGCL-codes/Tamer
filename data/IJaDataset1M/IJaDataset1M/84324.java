package com.mindtree.techworks.insight.releng.mvn.nsis.velocityutil;

import com.mindtree.techworks.insight.releng.mvn.nsis.actions.MojoInfo;
import com.mindtree.techworks.insight.releng.mvn.nsis.model.NsisProject;

/**
 * An NSIS tool to handle language settings for the NSIS compiler
 * 
 * If you set the instance with a key of 'langTool', then you can check the 
 * value of references using:
 * 
 * <pre>
 * 	$langTool.defaultLanguageName		-&gt; Returns the name of the default
 * 										language. Example - English
 *  $langTool.defaultLanguageNsisFormat -&gt; Returns the NSIS formatted version
 *  									of the default language. Example - 
 *  									${LANG_ENGLISH}
 * </pre>
 * 
 * @author <a href="mailto:bindul_bhowmik@mindtree.com">Bindul Bhowmik</a>
 * @version $Revision: 235 $ $Date: 2009-03-27 17:00:18 -0400 (Fri, 27 Mar 2009) $
 * 
 * @plexus.component role="com.mindtree.techworks.insight.releng.mvn.nsis.velocityutil.VelocityTool" role-hint="lang-tool"
 */
public class NSISLanguageTool implements VelocityTool {

    /**
	 * The Nsis Project
	 */
    private NsisProject nsisProject;

    public void setMojoInfo(MojoInfo mojoInfo) {
        this.nsisProject = mojoInfo.getNsisProject();
    }

    /**
	 * Returns the default language for the installer
	 * 
	 * @return The default language of the installer
	 * @see #getDefaultLanguageNsisFormat()
	 */
    public String getDefaultLanguageName() {
        return nsisProject.getProjectInfo().getDefaultInstallerLanguage();
    }

    /**
	 * Returns the default language of the installer in the NSIS format. So, if
	 * English is the default language, this method will return 
	 * <code>${LANG_ENGLISH}</code>.
	 * 
	 * @return The NSIS formatted default language.
	 * @see #getDefaultLanguageName()
	 */
    public String getDefaultLanguageNsisFormat() {
        StringBuffer buffer = new StringBuffer("${LANG_");
        buffer.append(getDefaultLanguageName());
        buffer.append('}');
        return buffer.toString();
    }
}
