package net.sf.refactorit.netbeans.common.vcs;

import net.sf.refactorit.common.util.Assert;
import net.sf.refactorit.common.util.ReflectionUtil;
import org.apache.log4j.Logger;
import org.netbeans.modules.vcscore.VcsFileSystem;
import org.netbeans.modules.vcscore.commands.VcsCommand;
import org.netbeans.modules.vcscore.util.VariableInputComponent;
import org.netbeans.modules.vcscore.util.VariableInputDescriptor;
import org.netbeans.modules.vcscore.util.VariableInputFormatException;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

class NonIteractiveExecution implements RestorableSetting {

    private static final Logger log = Logger.getLogger(NonIteractiveExecution.class);

    private static final String GLOBAL_INPUT_DESCRIPTOR = "GLOBAL_INPUT_DESCRIPTOR";

    private static final String INPUT_DESCRIPTOR_PARSED_NAME = "_For_Internal_Use_Only__INPUT_DESCRIPTOR_PARSED";

    private boolean vcsfsPromptForVarsForEachFile = false;

    private boolean vcsfsPromptForEditOn = false;

    private VariableInputDescriptor cmdParsedInputDescriptor = null;

    private Object cmdConcurrentExecution = null;

    private Object cmdNotificationSuccessMsg = null;

    private Object cmdDisplayPlainOutput = null;

    private Object cmdConfirmationMsg = null;

    private Object cmdCleanUnimportantFilesOnSucess = null;

    private String cmdExec = null;

    private Object cmdRefreshCurrentFolder = null;

    private final Hashtable vars;

    private final VcsFileSystem fs;

    private final VcsCommand cmd;

    NonIteractiveExecution(Hashtable vars, VcsFileSystem fs, VcsCommand cmd) {
        this.vars = vars;
        this.fs = fs;
        this.cmd = cmd;
    }

    void apply() {
        vars.put("SHOW", "DONT_SHOW");
        vcsfsPromptForVarsForEachFile = fs.isPromptForVarsForEachFile();
        fs.setPromptForVarsForEachFile(false);
        vcsfsPromptForEditOn = fs.isPromptForEditOn();
        fs.setPromptForEditOn(false);
        try {
            setupVarsFromCommand(cmd, vars);
        } catch (Exception e) {
            if (Assert.enabled) {
                log.warn("VcsCommand.PROPERTY_INPUT_DESCRIPTOR: " + VcsCommand.PROPERTY_INPUT_DESCRIPTOR);
                log.warn(e.getMessage(), e);
            }
        }
        try {
            setupVarsFromGlobalDescriptor(fs, vars);
        } catch (Exception e) {
            if (Assert.enabled) {
                log.warn("CommandExecutorSupport.GLOBAL_INPUT_DESCRIPTOR: " + GLOBAL_INPUT_DESCRIPTOR);
                log.warn(e.getMessage(), e);
            }
        }
        cmdConcurrentExecution = cmd.getProperty(VcsCommand.PROPERTY_CONCURRENT_EXECUTION);
        cmd.setProperty(VcsCommand.PROPERTY_CONCURRENT_EXECUTION, new Integer(VcsCommand.EXEC_SERIAL_ALL));
        cmdNotificationSuccessMsg = cmd.getProperty(VcsCommand.PROPERTY_NOTIFICATION_SUCCESS_MSG);
        cmd.setProperty(VcsCommand.PROPERTY_NOTIFICATION_SUCCESS_MSG, null);
        cmdDisplayPlainOutput = cmd.getProperty(VcsCommand.PROPERTY_DISPLAY_PLAIN_OUTPUT);
        cmd.setProperty(VcsCommand.PROPERTY_DISPLAY_PLAIN_OUTPUT, Boolean.FALSE);
        cmdConfirmationMsg = cmd.getProperty(VcsCommand.PROPERTY_CONFIRMATION_MSG);
        cmd.setProperty(VcsCommand.PROPERTY_CONFIRMATION_MSG, null);
        cmdExec = (String) cmd.getProperty(VcsCommand.PROPERTY_EXEC);
        if (cmdExec != null && cmdExec.startsWith("$[? CONFIRMATION_MSG]")) {
            int start = cmdExec.indexOf("] [");
            if (start >= 0) {
                int end = cmdExec.indexOf("] [", start + 1);
                if (end >= 0) {
                    String strippedExec = cmdExec.substring(start + 3, end);
                    cmd.setProperty(VcsCommand.PROPERTY_EXEC, strippedExec);
                }
            }
        }
        cmdCleanUnimportantFilesOnSucess = cmd.getProperty(VcsCommand.PROPERTY_CLEAN_UNIMPORTANT_FILES_ON_SUCCESS);
        cmd.setProperty(VcsCommand.PROPERTY_CLEAN_UNIMPORTANT_FILES_ON_SUCCESS, Boolean.FALSE);
        cmdRefreshCurrentFolder = cmd.getProperty(VcsCommand.PROPERTY_REFRESH_PARENT_FOLDER);
        cmd.setProperty(VcsCommand.PROPERTY_REFRESH_PARENT_FOLDER, Boolean.TRUE);
        vars.put("CHECKOUT_RO_RW", "");
        vars.put("QUIETNESS", "-Q");
        if (!"GET".equals(cmd.getName()) && !"GETR".equals(cmd.getName())) {
            vars.put("RW_MODE", "-W");
        } else {
            vars.put("RW_MODE", "");
        }
    }

    public void restore() {
        fs.setPromptForVarsForEachFile(vcsfsPromptForVarsForEachFile);
        fs.setPromptForEditOn(vcsfsPromptForEditOn);
        cmd.setProperty(VcsCommand.PROPERTY_CONCURRENT_EXECUTION, cmdConcurrentExecution);
        cmd.setProperty(VcsCommand.PROPERTY_NOTIFICATION_SUCCESS_MSG, cmdNotificationSuccessMsg);
        cmd.setProperty(VcsCommand.PROPERTY_DISPLAY_PLAIN_OUTPUT, cmdDisplayPlainOutput);
        cmd.setProperty(VcsCommand.PROPERTY_CONFIRMATION_MSG, cmdConfirmationMsg);
        cmd.setProperty(VcsCommand.PROPERTY_EXEC, cmdExec);
        cmd.setProperty(INPUT_DESCRIPTOR_PARSED_NAME, cmdParsedInputDescriptor);
        cmd.setProperty(VcsCommand.PROPERTY_CLEAN_UNIMPORTANT_FILES_ON_SUCCESS, cmdCleanUnimportantFilesOnSucess);
        cmd.setProperty(VcsCommand.PROPERTY_REFRESH_PARENT_FOLDER, cmdRefreshCurrentFolder);
    }

    private void setupVarsFromCommand(VcsCommand cmd, Hashtable vars) {
        cmdParsedInputDescriptor = (VariableInputDescriptor) cmd.getProperty(INPUT_DESCRIPTOR_PARSED_NAME);
        String inputDescriptorStr = (String) cmd.getProperty(VcsCommand.PROPERTY_INPUT_DESCRIPTOR);
        if (inputDescriptorStr != null) {
            try {
                VariableInputDescriptor inputDescriptor = createInputDescriptor(inputDescriptorStr, vars);
                cmd.setProperty(INPUT_DESCRIPTOR_PARSED_NAME, inputDescriptor);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }
    }

    private VariableInputDescriptor createInputDescriptor(String inputDescriptorStr, Hashtable vars) {
        VariableInputDescriptor inputDescriptor = (VariableInputDescriptor) ReflectionUtil.invokeMethod(VariableInputDescriptor.class, "parseItems", String.class, inputDescriptorStr);
        setupVarsFromDescriptor(inputDescriptor, vars);
        return inputDescriptor;
    }

    private void setupVarsFromGlobalDescriptor(final VcsFileSystem fs, final Hashtable vars) {
        String inputDescriptorStr = (String) fs.getVariablesAsHashtable().get(GLOBAL_INPUT_DESCRIPTOR);
        if (inputDescriptorStr != null) {
            try {
                createInputDescriptor(inputDescriptorStr, vars);
            } catch (VariableInputFormatException e) {
                log.warn(e.getMessage(), e);
            }
        }
    }

    private void setupVarsFromDescriptor(VariableInputDescriptor inputDescriptor, Hashtable vars) {
        inputDescriptor.setValuesAsDefault();
        VariableInputComponent[] components = inputDescriptor.components();
        for (int i = 0; i < components.length; i++) {
            final VariableInputComponent component = components[i];
            final String variable = component.getVariable();
            String value = component.getDefaultValue();
            component.setVarConditions(new String[] { "SHOW", null });
            if ("DESCRIPTION_FILE".equals(variable) && (value == null || value.length() == 0)) {
                try {
                    final File tempFile = File.createTempFile("tempVcsCmd", "input");
                    tempFile.deleteOnExit();
                    String fileName = tempFile.getAbsolutePath();
                    vars.put(variable, fileName);
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            } else if (value != null) {
                vars.put(variable, value);
            }
        }
    }
}
