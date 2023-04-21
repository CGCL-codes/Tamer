package jtk.project4.fleet.field;

import jtk.project4.fleet.domain.Attachments;
import jtk.project4.fleet.domain.Equipment;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.FormLayout.Align;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.MemoField;
import nl.coderight.jazz.form.field.TextField;
import nl.coderight.jazz.form.field.button.PushButton;

public class AddAttachmentField extends GroupControl<Attachments> {

    private MemoField attachmentField;

    private TextField descriptionField;

    private PushButton selectButton;

    public AddAttachmentField(String bindID) {
        setTitle("header.Attachments");
        setBindID(bindID);
        createFields();
        createLayout();
    }

    private void createFields() {
        attachmentField = new MemoField("path");
        attachmentField.setEditable(true);
        attachmentField.setRows(5);
        selectButton = new PushButton("select");
        descriptionField = new TextField("description", 30);
    }

    private void createLayout() {
        setLayout(new FormLayout()).addField(selectButton, Align.RIGHT).addRow().addLabel("Attachment", Align.LEFT).addRow().addField(attachmentField).addRow().addLabel("Descripton", Align.LEFT).addRow().addField(descriptionField).addRow();
        ;
    }
}
