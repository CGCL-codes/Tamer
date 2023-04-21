package org.monet.modelling.application.core.wizards.pages;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class PageCreateNewForm extends WizardNewFileCreationPage {

    private String code;

    public PageCreateNewForm(IStructuredSelection selection) {
        super("Monet Form File Wizard", selection);
        setTitle("Form File Wizard");
        setDescription("Create a Form File");
        setFileExtension("xml");
        setCode();
    }

    @Override
    protected InputStream getInitialContents() {
        String xmlTemplate = "<definition code=\"" + code + "\" " + " name=\"name\"" + ">";
        xmlTemplate += "\n  <form ></form > \n</definition>";
        return new ByteArrayInputStream(xmlTemplate.getBytes());
    }

    private void setCode() {
        UUID autoCode = UUID.randomUUID();
        code = autoCode.toString().replaceAll("-", "");
    }

    public String getCode() {
        return this.code;
    }
}
