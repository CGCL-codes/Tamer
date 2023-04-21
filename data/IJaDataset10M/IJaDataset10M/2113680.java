package com.softaspects.jsf.support.renderers.settings;

public interface Settings {

    public static final String PATH_TO_IMAGES = "pathToImages";

    public static final String PATH_TO_RENDERERS = "pathToRenderers";

    public static final String PATH_TO_JAVASCRIPTS_EXPLORER = "pathToJavaScriptsExplorer";

    public static final String PATH_TO_JAVASCRIPTS_NETSCAPE = "pathToJavaScriptsNetscape";

    public static final String PATH_TO_JAVASCRIPTS_ICE = "pathToJavaScriptsIce";

    public static final String PATH_TO_JAVASCRIPTS_OPERA = "pathToJavaScriptsOpera";

    public static final String SUFFIX_PATH_TO_JAVASCRIPTS_EXPLORER = "suffixPathToJavaScriptsExplorer";

    public static final String SUFFIX_PATH_TO_JAVASCRIPTS_NETSCAPE = "suffixPathToJavaScriptsNetscape";

    public static final String SUFFIX_PATH_TO_JAVASCRIPTS_OPERA = "suffixPathToJavaScriptsOpera";

    public static final String SUFFIX_PATH_TO_JAVASCRIPTS_ICE = "suffixPathToJavaScriptsIce";

    public static final String VARIABLE_LISTING = "variableListing";

    public static final String APPLICATION_ROOT_CONTEXT = "applicationRootContext";

    public static final String SERVLET_CONTEXT = "servletContext";

    public static final String TREE_DYNAMIC_DATA_MODEL_PROVIDER_SERVLET_URL = "treeDynamicDataModelProviderServletUrl";

    public static final String PARAMS = "params";

    public static final String[] SETTINGS_LIST = new String[] { PATH_TO_IMAGES, PATH_TO_RENDERERS, PATH_TO_JAVASCRIPTS_EXPLORER, PATH_TO_JAVASCRIPTS_NETSCAPE, PATH_TO_JAVASCRIPTS_ICE, PATH_TO_JAVASCRIPTS_OPERA, SUFFIX_PATH_TO_JAVASCRIPTS_EXPLORER, SUFFIX_PATH_TO_JAVASCRIPTS_NETSCAPE, SUFFIX_PATH_TO_JAVASCRIPTS_OPERA, SUFFIX_PATH_TO_JAVASCRIPTS_ICE, VARIABLE_LISTING, APPLICATION_ROOT_CONTEXT, SERVLET_CONTEXT, TREE_DYNAMIC_DATA_MODEL_PROVIDER_SERVLET_URL, PARAMS };

    public Object getParam(String paramName);

    public void setParam(String paramName, Object paramValue);

    public boolean isSetParam(String paramName);

    public String[] getKeys();
}
