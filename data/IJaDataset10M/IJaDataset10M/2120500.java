package org.t2framework.vili.eclipse;

public interface Globals extends org.t2framework.vili.Globals {

    String CLASSPATH_CONTAINER_M2ECLIPSE = BUNDLENAME_M2ECLIPSE + ".MAVEN2_CLASSPATH_CONTAINER";

    String CLASSPATH_CONTAINER_M2ECLIPSE_LIGHT = BUNDLENAME_M2ECLIPSE_LIGHT + ".MAVEN2_CLASSPATH_CONTAINER";

    String CLASSPATH_CONTAEINR_JRE = "org.eclipse.jdt.launching.JRE_CONTAINER";

    String ENCODING = "UTF-8";

    String HEAD_BEHAVIOR_PROPERTIES = PATH_VILI_INF + "/behavior_";

    String TAIL_BEHAVIOR_PROPERTIES = ".properties";

    String IMAGE_VILI = "icons/vili.gif";

    String PATH_VILI_API_POM_PROPERTIES = "META-INF/maven/org.t2framework.vili/vili-api/pom.properties";

    String KEY_VERSION = "version";

    String PATH_M2ECLIPSE_PREFS = ".settings/org.maven.ide.eclipse.prefs";

    String PATH_M2ECLIPSE_LIGHT_PREFS = ".settings/org.maven.ide.eclipse_light.prefs";

    String PATH_MAVEN2ADDITIONAL_PREFS = ".settings/net.skirnir.eclipse.maven.prefs";

    String QUALIFIERPREFIX_MOLD = Activator.PLUGIN_ID + ".mold.";

    String BEHAVIOR_PROP_PROJECTBUILDER_OMITADDINGWEBNATURES = "projectBuilder.omitAddingWebNatures";

    String PROP_LOCALREPOSITORY = "localRepository";

    String SYSPROP_USER_HOME = "user.home";

    String PLACEHOLDER_USER_HOME = "${" + SYSPROP_USER_HOME + "}";

    String ENV_M2_HOME = "M2_HOME";

    String NAME_SETTINGS_XML = "settings.xml";
}
