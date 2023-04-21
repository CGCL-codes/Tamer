package org.jostraca.process;

import org.jostraca.Property;
import org.jostraca.DefaultPropertySets;
import org.jostraca.Template;
import org.jostraca.TemplatePath;
import org.jostraca.TemplateException;
import org.jostraca.util.FileUtil;
import org.jostraca.util.Standard;
import org.jostraca.util.ErrorUtil;
import org.jostraca.util.ValueSet;
import org.jostraca.util.ValueCode;
import org.jostraca.util.PropertySet;
import org.jostraca.util.PropertySetManager;
import org.jostraca.util.PropertySetModifierManager;
import java.util.List;
import java.io.File;

/** Processing class for preparing the generation process by setting special properties.
 */
public class GenericPreparer extends TemplateHandlerSupport {

    public static final char[] sCanonicalTemplateFileNameChars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'u', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'U', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '$', '_' };

    protected PropertySetModifierManager iPropertySetModifierManager = new PropertySetModifierManager();

    protected PropertySetManager iLangPSM = DefaultPropertySets.makeLang();

    public GenericPreparer() {
    }

    protected void processImpl(Template pTemplate) {
        Template template = pTemplate;
        PropertySet tmps = pTemplate.getMergedPropertySet();
        String codeWriterLang = template.getCodeWriterLang();
        PropertySet langPS = loadLangPropertySet(codeWriterLang, tmps);
        mUserMessageHandler.debug("Template Script:", codeWriterLang);
        template.setPropertySet(CONF_lang, langPS);
        handleCodeWriterName(template);
        overridePropertySet(template, iPropertySetModifierManager);
        template.modifyForOldVersion();
        validateMergedPropertySet(template.getMergedPropertySet());
        tmps = template.getMergedPropertySet();
        TemplatePath tp = template.getTemplatePath();
        dumpPropertySet(tmps, tp);
        dumpTemplateSource(tmps, tp, template);
    }

    protected void completeImpl(List pTemplateList) {
    }

    public static void handleCodeWriterName(Template pTemplate) {
        PropertySet mps = pTemplate.getMergedPropertySet();
        PropertySet ops = pTemplate.getPropertySet(CONF_override);
        if (!mps.isDefined(Property.main_CodeWriter)) {
            String templateFileName = mps.get(Property.jostraca_template_file);
            templateFileName = FileUtil.removeExtension(templateFileName);
            templateFileName = makeCanonicalTemplateFileName(templateFileName);
            ops.set(Property.main_CodeWriter, templateFileName + FILE_EXT_Writer);
        }
    }

    /** Insert system properties into current PropertySet */
    public static void overridePropertySet(Template pTemplate, PropertySetModifierManager pPSMM) {
        PropertySet mps = pTemplate.getMergedPropertySet();
        PropertySet ops = pTemplate.getPropertySet(CONF_override);
        String[] sysProps = { Property.jostraca_system_pathSeparator, File.pathSeparator, Property.jostraca_system_fileSeparator, File.separator };
        String sysPropName;
        String sysPropDefault;
        int numProps = sysProps.length / 2;
        for (int propI = 0; propI < numProps; propI++) {
            sysPropName = sysProps[propI * 2];
            sysPropDefault = sysProps[(propI * 2) + 1];
            if (!mps.isDefined(sysPropName)) {
                ops.set(sysPropName, sysPropDefault);
            }
        }
        if (!mps.isDefined(Property.main_OutputFolder)) {
            ops.set(Property.main_OutputFolder, Standard.DOT);
        }
        if (mps.isYes(Property.main_MakeBackup)) {
            ops.set(Property.jostraca_MakeBackup, mps.get(Property.lang_TrueString));
        } else {
            ops.set(Property.jostraca_MakeBackup, mps.get(Property.lang_FalseString));
        }
        if (null != pPSMM) {
            String modClasses = mps.get(Property.jostraca_properties_modifiers);
            if (mps.has(Property.jostraca_properties_modifiers)) {
                pPSMM.loadPropertySetModifierClasses(modClasses);
                pPSMM.modify(mps, ops);
            }
        }
    }

    /** Load lang PropertySet based on lang code.
   *  Return an empty property set if not found.
   *  @param pLangCode Used to find file in lang config folder: lang.conf
   */
    private PropertySet loadLangPropertySet(String pLangCode, PropertySet pPropertySet) {
        PropertySet langPS = new PropertySet();
        String configFolder = pPropertySet.get(Property.jostraca_ConfigFolder);
        File langPropFilePath = new File(configFolder, buildLangConfigFileName(pLangCode));
        try {
            langPS = iLangPSM.load(pLangCode, langPropFilePath, PropertySetManager.USE_DEFAULT_IF_FILE_DOES_NOT_EXIST);
        } catch (Exception e) {
            ErrorUtil.nonFatalException(e);
        }
        return langPS;
    }

    /** Validate merged PropertySet.
   *  REVIEW: what relation to Jostraca.validateConfig?
   *  @param pPropertySet PropertySet to be validated.
   */
    private void validateMergedPropertySet(PropertySet pPropertySet) throws TemplateException {
        if (!pPropertySet.isNo(Property.jostraca_strict_version)) {
            String version = pPropertySet.get(Property.main_JostracaVersion);
            if (!version.startsWith(VERSION_NUMBER)) {
                if (version.startsWith("0.")) {
                } else {
                    throw new TemplateException(TemplateException.CODE_bad_version, new String[] { "template_version", version, "expected_version", VERSION_NUMBER });
                }
            }
        }
    }

    /** Delete all non-canonical chars. Makes filenames usable as Java class names. */
    private static String makeCanonicalTemplateFileName(String pTemplateFileName) {
        String tfn = pTemplateFileName;
        if (null == tfn) {
            tfn = Standard.EMPTY;
        }
        StringBuffer canonicalB = new StringBuffer(tfn.length());
        int numChars = tfn.length();
        next_char: for (int cI = 0; cI < numChars; cI++) {
            char c = tfn.charAt(cI);
            for (int tI = 0; tI < sCanonicalTemplateFileNameChars.length; tI++) {
                if (c == sCanonicalTemplateFileNameChars[tI]) {
                    if (0 == cI) {
                        c = Character.toUpperCase(c);
                    }
                    canonicalB.append(c);
                    continue next_char;
                }
            }
        }
        return canonicalB.toString();
    }

    /** Build lang config file name: append .conf
   *  @param pLangCode Code name of language.
   */
    private String buildLangConfigFileName(String pLangCode) {
        return Standard.EMPTY + pLangCode + FILE_EXT_conf;
    }

    /** Dump property set to external file in the output folder. */
    private void dumpPropertySet(PropertySet pPropertySet, TemplatePath pTemplatePath) {
        if (pPropertySet.isYes(Property.main_DumpPropertySet)) {
            String dumpFile = Standard.EMPTY;
            try {
                dumpFile = pPropertySet.get(Property.main_OutputFolder, Standard.DOT) + File.separator + pTemplatePath.getTemplateFileName() + "-jostraca-settings.txt";
                pPropertySet.save(dumpFile);
            } catch (Exception e) {
                throw ProcessException.CODE_dump_ps(new ValueSet(ValueCode.FILE, dumpFile), e);
            }
        }
    }

    /** Dump template source to external file in the output folder. */
    private void dumpTemplateSource(PropertySet pPropertySet, TemplatePath pTemplatePath, Template pTemplate) {
        if (pPropertySet.isYes(Property.main_DumpTemplate)) {
            String dumpFile = Standard.EMPTY;
            try {
                dumpFile = pPropertySet.get(Property.main_OutputFolder, Standard.DOT) + File.separator + pTemplatePath.getTemplateFileName() + "-template-source.txt";
                FileUtil.writeFile(dumpFile, pTemplate.getSource());
            } catch (Exception e) {
                throw ProcessException.CODE_dump_tm(new ValueSet(ValueCode.FILE, dumpFile), e);
            }
        }
    }
}
