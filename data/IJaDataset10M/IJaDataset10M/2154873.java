package org.jostraca.directive;

import java.io.File;
import java.util.*;
import org.jostraca.BasicTemplatePath;
import org.jostraca.Property;
import org.jostraca.Template;
import org.jostraca.TemplateException;
import org.jostraca.TemplatePath;
import org.jostraca.util.FileUtil;
import org.jostraca.util.PropertySet;
import org.jostraca.util.RegExp;
import org.jostraca.util.Standard;
import org.jostraca.util.ValueCode;

public class BlockIncludeDirective extends IncludeDirectiveSupport {

    public static final String NAME = "include-block";

    public static final int MAX_INCLUDE_COUNT = 111;

    public String getName() {
        return NAME;
    }

    protected String loadIncludeSource(String pPath, List pArguments, Template pTemplate) throws DirectiveException {
        File includeBaseFolder = (File) pTemplate.getAttribute(IncludeBaseDirective.class.getName() + ":includeBase");
        String includeFilePath = null == includeBaseFolder ? pPath : new File(includeBaseFolder, pPath).getAbsolutePath();
        try {
            TemplatePath tmpath = pTemplate.getTemplatePath();
            PropertySet tmps = pTemplate.getMergedPropertySet();
            String includeBase = tmpath.getTemplateFolder();
            if (pArguments.contains(INCLUDE_MOD_output_relative)) {
                includeBase = tmps.get(Property.main_OutputFolder);
            }
            File includeF = new File(pPath);
            if (includeF.isAbsolute() || pPath.startsWith("/") || pPath.startsWith("\\")) {
                includeFilePath = pPath;
            } else {
                includeFilePath = includeBase + File.separatorChar + pPath;
            }
            boolean onlyifexists = pArguments.contains(INCLUDE_MOD_if_exists);
            String includeFileContent = FileUtil.readFile(includeFilePath, (onlyifexists ? FileUtil.EMPTY_IF_IO_ERROR : FileUtil.FAIL_ON_IO_ERROR));
            String includeBlockMark = (String) pArguments.get(0);
            RegExp markRegExp = RegExp.make(includeBlockMark, RegExp.ModeSet.DotMatchesNewline);
            String includeBlockContent = markRegExp.match(includeFileContent);
            return includeBlockContent;
        } catch (Exception e) {
            throw new TemplateException(TemplateException.CODE_includeblock, new String[] { ValueCode.FILE, includeFilePath, ValueCode.TEMPLATE, pPath }, e);
        }
    }
}
