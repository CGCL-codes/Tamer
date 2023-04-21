package br.ufma.sgdu.window;

import java.util.Date;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import br.ufma.sgdu.database.DbDocManager;
import br.ufma.sgdu.entity.Document;
import br.ufma.sgdu.util.DocumentAnalizer;
import br.ufma.sgdu.util.FileSupportVerificator;
import br.ufma.sgdu.util.Reader2StringConverter;

/**
 * <b>AdminUploadWindow</b><br/>
 * Essa e uma classe de "apoio" a AdminWindow. Gerencia a interface de upload
 * de documentos.
 */
public class AdminUploadWindow extends SGDUSecureWindow {

    private static final long serialVersionUID = 7268970269306314382L;

    private Media doc = null;

    private Media[] images = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void goBack() {
        if (!authenticated) {
            Executions.sendRedirect("/unauthorized.zul");
            return;
        }
        Executions.sendRedirect("/admin_main.zul");
    }

    /**
	 * Botao "?" do upload de documento
	 */
    public void helpDoc() {
        if (!authenticated) {
            Executions.sendRedirect("/unauthorized.zul");
            return;
        }
        try {
            Messagebox.show("Ao enviar um documento de texto, " + "o sistema extrai o conteúdo automaticamente e o armazena " + "para que possa ser consultado posteriormente. Clique em \"" + "Upload\", selecione o documento e depois clique em \"" + "Enviar\". Mas atenção, os formatos de arquivo aceitos pelo " + "sistema são:\n" + FileSupportVerificator.DocDescription(), "Ajuda", Messagebox.OK, Messagebox.INFORMATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Botao "?" do upload de documento
	 */
    public void helpImage() {
        if (!authenticated) {
            Executions.sendRedirect("/unauthorized.zul");
            return;
        }
        try {
            Messagebox.show("Ao enviar um documento digitalizado, o sistema " + "extrai o texto automaticamente e o armazena para que possa " + "ser consultado posteriormente. Primeiro selecione o número " + "de imagens que compõem o documento. Depois clique em \"Upload\" " + "e selecione cada uma das imagens. " + "Finalmente clique em \"Enviar\". Mas atenção, certifique-se de " + "que as páginas escaneadas do documento estejam legíveis e em " + "boa qualidade. Os formatos de imagem aceitos pelo sistema são:\n" + FileSupportVerificator.ImageDescription(), "Ajuda", Messagebox.OK, Messagebox.INFORMATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Botao Upload - Documento de Texto
	 */
    public void uploadDoc() {
        if (!authenticated) {
            Executions.sendRedirect("/unauthorized.zul");
            return;
        }
        try {
            Media docaux = Fileupload.get();
            if (docaux != null) {
                if (FileSupportVerificator.isDocumentValid(docaux.getName())) {
                    doc = docaux;
                    ((Textbox) getFellow("fileUploadedTextBox")).setValue(docaux.getName());
                } else {
                    Messagebox.show("Esse tipo de arquivo não é suportado. Clique no botão " + "\"?\" para mais informações.", "Arquivo inválido", Messagebox.OK, Messagebox.ERROR);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
	 * Botao Upload - Imagens
	 */
    public void uploadImage() {
        if (!authenticated) {
            Executions.sendRedirect("/unauthorized.zul");
            return;
        }
        int numImages = getUploadImageNumber();
        try {
            Media[] imagesaux = Fileupload.get(numImages);
            if (imagesaux == null) return;
            for (int i = 0; i < imagesaux.length; i++) {
                if (imagesaux[i] != null) {
                    if (!FileSupportVerificator.isImageValid(imagesaux[i].getName())) {
                        Messagebox.show(imagesaux[i].getName() + " Esse tipo de arquivo não " + "é suportado. Clique no botão \"?\" para mais informações.", "Arquivo inválido", Messagebox.OK, Messagebox.ERROR);
                        return;
                    }
                }
            }
            images = imagesaux;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        String r = "";
        if (images != null) {
            for (int i = 0; i < images.length; i++) {
                if (images[i] == null) continue;
                r += images[i].getName() + "\n";
            }
        }
        ((Textbox) getFellow("fileUploadedTextBox")).setValue(r);
    }

    /**
	 * Chame ao checar o radio group
	 */
    public void radioEvent() {
        if (!authenticated) {
            Executions.sendRedirect("/unauthorized.zul");
            return;
        }
        Button docUploadButton = (Button) getFellow("docUploadButton");
        Button imageUploadButton = (Button) getFellow("imageUploadButton");
        Spinner imageSpinner = (Spinner) getFellow("imageSpinner");
        Textbox fileUploadedTextBox = (Textbox) getFellow("fileUploadedTextBox");
        docUploadButton.setDisabled(!docUploadButton.isDisabled());
        imageUploadButton.setDisabled(!imageUploadButton.isDisabled());
        imageSpinner.setDisabled(imageUploadButton.isDisabled());
        if (!docUploadButton.isDisabled()) {
            if (doc != null) {
                fileUploadedTextBox.setValue(doc.getName());
            } else {
                fileUploadedTextBox.setValue("");
            }
        } else if (!imageUploadButton.isDisabled()) {
            if (images != null) {
                String r = "";
                for (int i = 0; i < images.length; i++) {
                    if (images[i] == null) continue;
                    r += images[i].getName() + "\n";
                }
                fileUploadedTextBox.setValue(r);
            } else {
                fileUploadedTextBox.setValue("");
            }
        }
    }

    private int getUploadImageNumber() {
        Spinner spinner = (Spinner) getFellow("imageSpinner");
        if (spinner.intValue() < 1) {
            spinner.setValue(1);
        } else if (spinner.intValue() > 999) {
            spinner.setValue(999);
        }
        spinner.invalidate();
        return spinner.getValue();
    }

    /**
	 * Botao "Enviar"
	 * @throws Exception 
	 */
    public void send() throws Exception {
        if (!authenticated) {
            Executions.sendRedirect("/unauthorized.zul");
            return;
        }
        String title = ((Textbox) getFellow("titleTextBox")).getValue();
        String description = ((Textbox) getFellow("descriptionTextBox")).getValue();
        Date date = ((Datebox) getFellow("dateBox")).getValue();
        boolean isDocMediaType = ((Radiogroup) getFellow("typeFileRadioGroup")).getSelectedIndex() != 1;
        if (isEmpty(title)) {
            try {
                Messagebox.show("O título está em branco. O preenchimeto desse campo é " + "obrigatório.", "Erro no título", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (isEmpty(description)) {
            try {
                Messagebox.show("A descrição está em branco. O preenchimeto desse campo é " + "obrigatório.", "Erro na descrição", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (date == null) {
            try {
                Messagebox.show("A data de expedição está em branco. O preenchimeto desse campo é " + "obrigatório.", "Erro na data de expedição", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        boolean fail = false;
        if (isDocMediaType) {
            fail = (doc == null);
        } else {
            if (images == null) {
                fail = true;
            } else {
                for (int i = 0; i < images.length; i++) {
                    if (images[i] != null) break;
                    if (i == images.length - 1) fail = true;
                }
            }
        }
        if (fail) {
            try {
                Messagebox.show("Você não fez upload de nenhum" + (isDocMediaType ? " documento." : "a imagem."), "Falta de documento", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        DbDocManager dbDocManager = new DbDocManager();
        String content = "";
        if (isDocMediaType) {
            if (FileSupportVerificator.isPDFType(doc.getName())) {
                content = getContent(doc, FileSupportVerificator.PDF_TYPE);
                dbDocManager.insertDocument(title, description, date, new Date(System.currentTimeMillis()), dbAccGate.getAdministrator().getLogin(), content, Document.PORTABLE_DOCUMENT_FILE_TYPE);
                insertFile(doc, dbDocManager, FileSupportVerificator.PDF_TYPE);
            } else if (FileSupportVerificator.isDOCType(doc.getName())) {
                content = getContent(doc, FileSupportVerificator.DOC_TYPE);
                dbDocManager.insertDocument(title, description, date, new Date(System.currentTimeMillis()), dbAccGate.getAdministrator().getLogin(), content, Document.MICROSOFT_WORD_TYPE);
                insertFile(doc, dbDocManager, FileSupportVerificator.DOC_TYPE);
            } else if (FileSupportVerificator.isDOCXType(doc.getName())) {
                content = getContent(doc, FileSupportVerificator.DOCX_TYPE);
                dbDocManager.insertDocument(title, description, date, new Date(System.currentTimeMillis()), dbAccGate.getAdministrator().getLogin(), content, Document.MICROSOFT_WORD_TYPE);
                insertFile(doc, dbDocManager, FileSupportVerificator.DOCX_TYPE);
            } else if (FileSupportVerificator.isHTMLType(doc.getName())) {
                content = getContent(doc, FileSupportVerificator.HTML_TYPE);
                dbDocManager.insertDocument(title, description, date, new Date(System.currentTimeMillis()), dbAccGate.getAdministrator().getLogin(), content, Document.HIPERTEXT_MARKUP_TYPE);
                insertFile(doc, dbDocManager, FileSupportVerificator.HTML_TYPE);
            }
        } else {
            for (int i = 0; i < images.length; i++) {
                if (images[i] == null) continue;
                if (FileSupportVerificator.isPNGType(images[i].getName())) {
                    content += getContent(images[i], FileSupportVerificator.PNG_TYPE);
                } else if (FileSupportVerificator.isJPEGType(images[i].getName())) {
                    content += getContent(images[i], FileSupportVerificator.JPEG_TYPE);
                } else if (FileSupportVerificator.isGIFType(images[i].getName())) {
                    content += getContent(images[i], FileSupportVerificator.GIF_TYPE);
                } else if (FileSupportVerificator.isBMPType(images[i].getName())) {
                    content += getContent(images[i], FileSupportVerificator.BMP_TYPE);
                }
            }
            dbDocManager.insertDocument(title, description, date, new Date(System.currentTimeMillis()), dbAccGate.getAdministrator().getLogin(), content, Document.IMAGE_TYPE);
            for (int i = 0; i < images.length; i++) {
                if (images[i] == null) continue;
                if (FileSupportVerificator.isPNGType(images[i].getName())) {
                    insertFile(images[i], dbDocManager, FileSupportVerificator.PNG_TYPE);
                } else if (FileSupportVerificator.isJPEGType(images[i].getName())) {
                    insertFile(images[i], dbDocManager, FileSupportVerificator.JPEG_TYPE);
                } else if (FileSupportVerificator.isGIFType(images[i].getName())) {
                    insertFile(images[i], dbDocManager, FileSupportVerificator.GIF_TYPE);
                } else if (FileSupportVerificator.isBMPType(images[i].getName())) {
                    insertFile(images[i], dbDocManager, FileSupportVerificator.BMP_TYPE);
                }
            }
        }
        try {
            Messagebox.show("Documento inserido com sucesso!", "Sucesso na operação", Messagebox.OK, Messagebox.INFORMATION);
        } catch (InterruptedException e) {
        }
        Executions.sendRedirect("/admin_main.zul");
    }

    private boolean isEmpty(String str) {
        if (str == null) return true;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private String getContent(Media doc, short type) throws Exception {
        String content = "";
        switch(type) {
            case FileSupportVerificator.PDF_TYPE:
            case FileSupportVerificator.DOC_TYPE:
            case FileSupportVerificator.DOCX_TYPE:
                try {
                    content = DocumentAnalizer.getText(doc.getStreamData(), type);
                } catch (IllegalStateException e) {
                    try {
                        content = DocumentAnalizer.getText(doc.getStringData(), type);
                    } catch (IllegalStateException e1) {
                        content = DocumentAnalizer.getText(doc.getReaderData(), type);
                    }
                }
                break;
            case FileSupportVerificator.HTML_TYPE:
                try {
                    content = DocumentAnalizer.getText(doc.getStringData(), type);
                } catch (IllegalStateException e) {
                    try {
                        content = DocumentAnalizer.getText(doc.getReaderData(), type);
                    } catch (IllegalStateException e1) {
                        content = DocumentAnalizer.getText(doc.getStreamData(), type);
                    }
                }
                break;
            case FileSupportVerificator.PNG_TYPE:
            case FileSupportVerificator.JPEG_TYPE:
            case FileSupportVerificator.GIF_TYPE:
            case FileSupportVerificator.BMP_TYPE:
                try {
                    content = DocumentAnalizer.getTextOfImage(doc.getByteData(), type);
                } catch (IllegalStateException e) {
                    content = DocumentAnalizer.getTextOfImage(doc.getStreamData(), type);
                }
                break;
        }
        return content;
    }

    private void insertFile(Media doc, DbDocManager dbDocManager, short type) throws Exception {
        switch(type) {
            case FileSupportVerificator.PDF_TYPE:
            case FileSupportVerificator.DOC_TYPE:
            case FileSupportVerificator.DOCX_TYPE:
                try {
                    dbDocManager.insertFile(doc.getName().replace(' ', '_'), doc.getStreamData());
                } catch (IllegalStateException e) {
                    try {
                        dbDocManager.insertFile(doc.getName().replace(' ', '_'), doc.getStringData().getBytes());
                    } catch (IllegalStateException e1) {
                        dbDocManager.insertFile(doc.getName().replace(' ', '_'), Reader2StringConverter.get(doc.getReaderData()).getBytes());
                    }
                }
                break;
            case FileSupportVerificator.HTML_TYPE:
                try {
                    dbDocManager.insertFile(doc.getName().replace(' ', '_'), doc.getStringData().getBytes());
                } catch (IllegalStateException e) {
                    try {
                        dbDocManager.insertFile(doc.getName().replace(' ', '_'), Reader2StringConverter.get(doc.getReaderData()).getBytes());
                    } catch (IllegalStateException e1) {
                        dbDocManager.insertFile(doc.getName().replace(' ', '_'), doc.getStreamData());
                    }
                }
                break;
            case FileSupportVerificator.PNG_TYPE:
            case FileSupportVerificator.JPEG_TYPE:
            case FileSupportVerificator.GIF_TYPE:
            case FileSupportVerificator.BMP_TYPE:
                try {
                    dbDocManager.insertFile(doc.getName().replace(' ', '_'), doc.getByteData());
                } catch (IllegalStateException e) {
                    dbDocManager.insertFile(doc.getName().replace(' ', '_'), doc.getStreamData());
                }
                break;
        }
    }
}
