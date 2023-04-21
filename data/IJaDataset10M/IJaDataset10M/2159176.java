package com.jacum.cms.rcp.ui.editors.item.controls;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import com.jacum.cms.rcp.model.ContentSource;
import com.jacum.cms.rcp.model.Item;
import com.jacum.cms.rcp.ui.Activator;
import com.jacum.cms.rcp.ui.Messages;
import com.jacum.cms.rcp.ui.actions.AbstractServerAction;
import com.jacum.cms.rcp.ui.editors.item.preferences.PreferenceConstants;
import com.jacum.cms.rcp.ui.utils.ProgressBarUtils;
import com.jacum.cms.util.MimeTypeMapper;
import com.jacum.cms.ws.RemoteContentItemProperty;
import com.jacum.cms.ws.properties.BinaryProperty;

/**
 * @author rich
 *
 */
public class BinaryControl extends AbstractPropertyControl {

    private static final String IMAGE_TITLE = Messages.getString("BinaryControl.19");

    private static final Logger logger = Logger.getLogger(BinaryControl.class);

    private static final IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();

    private BinaryProperty binaryProperty;

    private Item item;

    private byte[] value;

    private Composite scrolledCompositeContent;

    private Composite scrolledComposite;

    private Canvas canvas;

    private Button loadImageButton;

    private Label unknownFormatLabel;

    private String guessedMimeType;

    /**
	 * @param parent
	 * @param name
	 */
    public BinaryControl(Composite parent, String name, Item item) {
        super(parent, name);
        this.item = item;
        scrolledCompositeContent = parent.getParent();
        scrolledComposite = scrolledCompositeContent.getParent();
    }

    @Override
    public void createControls() {
        super.createControls();
        final Shell shell = parent.getShell();
        final Composite binaryComposite = new Composite(parent, SWT.NONE);
        final GridLayout gridLayout = new GridLayout(4, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        binaryComposite.setLayout(gridLayout);
        final Label bytesLabel = new Label(binaryComposite, SWT.NONE);
        int size = 0;
        if (binaryProperty != null) {
            size = binaryProperty.getLength();
        } else {
            value = BinaryProperty.DEFAULT_VALUE;
        }
        final String sizeLabelTemplate = Messages.getString("BinaryControl.0");
        final BinaryControl control = this;
        bytesLabel.setText(MessageFormat.format(sizeLabelTemplate, new Object[] { size }));
        Button loadButton = new Button(binaryComposite, SWT.PUSH);
        loadButton.setText(Messages.getString("BinaryControl.1"));
        loadButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                String filePath = new FileDialog(parent.getShell(), SWT.OPEN).open();
                if (filePath != null) {
                    File file = new File(filePath);
                    value = new byte[(int) file.length()];
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        fileInputStream.read(value);
                        setDirty(true);
                        bytesLabel.setText(MessageFormat.format(sizeLabelTemplate, new Object[] { value.length }));
                        disposeWidget(canvas);
                        disposeWidget(loadImageButton);
                        disposeWidget(unknownFormatLabel);
                        if (value.length > 0) {
                            createImageArea(shell, binaryComposite);
                            guessedMimeType = guessMimeType(chopExtension(file));
                            editorPart.firePropertyValueChanged(control);
                        }
                    } catch (FileNotFoundException e1) {
                        logger.error(e1.getMessage(), e1);
                        String fileNotFoundMessage = MessageFormat.format(Messages.getString("BinaryControl.2"), new Object[] { filePath });
                        MessageDialog.openError(shell, Messages.getString("BinaryControl.3"), fileNotFoundMessage);
                    } catch (IOException e2) {
                        logger.error(e2.getMessage(), e2);
                        String errorMessage = MessageFormat.format(Messages.getString("BinaryControl.4"), new Object[] { filePath, e2.getMessage() });
                        MessageDialog.openError(shell, Messages.getString("BinaryControl.5"), errorMessage);
                    }
                }
            }
        });
        Button saveButton = new Button(binaryComposite, SWT.PUSH);
        saveButton.setText(Messages.getString("BinaryControl.6"));
        saveButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (binaryProperty != null) {
                    String filePath = new FileDialog(shell, SWT.SAVE).open();
                    if (filePath != null) {
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                            if (value == null) {
                                loadDataFromServer(shell);
                            }
                            fileOutputStream.write(value);
                            fileOutputStream.close();
                            String message = MessageFormat.format(Messages.getString("BinaryControl.7"), new Object[] { filePath });
                            MessageDialog.openInformation(shell, Messages.getString("BinaryControl.8"), message);
                        } catch (FileNotFoundException e1) {
                            logger.error(e1.getMessage(), e1);
                            String fileNotFoundMessage = MessageFormat.format(Messages.getString("BinaryControl.9"), new Object[] { filePath });
                            MessageDialog.openError(shell, Messages.getString("BinaryControl.10"), fileNotFoundMessage);
                        } catch (IOException e2) {
                            logger.error(e2.getMessage(), e2);
                            String errorMessage = MessageFormat.format(Messages.getString("BinaryControl.11"), new Object[] { filePath, e2.getMessage() });
                            MessageDialog.openError(shell, Messages.getString("BinaryControl.12"), errorMessage);
                        }
                    }
                } else {
                    MessageDialog.openWarning(shell, Messages.getString("BinaryControl.13"), Messages.getString("BinaryControl.14"));
                }
            }
        });
        if (size > 0) {
            if (value != null && value.length > 0) {
                createImageArea(shell, binaryComposite);
            } else {
                loadImageButton = new Button(binaryComposite, SWT.PUSH);
                loadImageButton.setText(Messages.getString("BinaryControl.15"));
                loadImageButton.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        loadDataFromServer(shell);
                        loadImageButton.dispose();
                        createImageArea(shell, binaryComposite);
                    }
                });
            }
        }
    }

    /**
	 * @param shell
	 * @param binaryComposite
	 */
    private void createImageArea(final Shell shell, Composite binaryComposite) {
        try {
            Display display = shell.getDisplay();
            final Image image = new Image(display, new ByteArrayInputStream(value));
            final Rectangle bounds = image.getBounds();
            int maxHeight = preferenceStore.getInt(PreferenceConstants.MAX_IMAGE_HEIGHT);
            int maxWidth = preferenceStore.getInt(PreferenceConstants.MAX_IMAGE_HEIGHT);
            canvas = new Canvas(binaryComposite, SWT.NONE);
            final Image canvasImage;
            if (bounds.height > maxHeight || bounds.width > maxWidth) {
                double widthScale = ((double) maxWidth) / bounds.width;
                double heightScale = ((double) maxHeight) / bounds.height;
                double k;
                if (widthScale > heightScale) {
                    k = heightScale;
                } else {
                    k = widthScale;
                }
                canvasImage = new Image(display, image.getImageData().scaledTo((int) (bounds.width * k), (int) (bounds.height * k)));
                canvas.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseDown(MouseEvent e) {
                        openImage(image, shell);
                    }
                });
                canvas.setCursor(new Cursor(display, SWT.CURSOR_HAND));
                canvas.setToolTipText(Messages.getString("BinaryControl.16"));
            } else {
                canvasImage = image;
            }
            GridData gridData = new GridData();
            Rectangle imageBounds = canvasImage.getBounds();
            gridData.widthHint = imageBounds.width;
            gridData.heightHint = imageBounds.height;
            canvas.setLayoutData(gridData);
            canvas.addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    e.gc.drawImage(canvasImage, 0, 0);
                }
            });
            resizeContentPane();
        } catch (SWTException e) {
            unknownFormatLabel = new Label(binaryComposite, SWT.NONE);
            unknownFormatLabel.setText(Messages.getString("BinaryControl.17"));
            resizeContentPane();
        }
    }

    private String chopExtension(File f) {
        String name = f.getName();
        if (name.indexOf('.') > 0) {
            return name.substring(name.lastIndexOf('.') + 1);
        }
        return "";
    }

    private String guessMimeType(String extension) {
        String mimeType = mimeTypeMap.get(extension.toLowerCase());
        if (mimeType != null) {
            return mimeType;
        }
        return "application/x-octet-stream";
    }

    private static final Map<String, String> mimeTypeMap = new HashMap<String, String>();

    static {
        mimeTypeMap.put("jpg", "image/jpeg");
        mimeTypeMap.put("gif", "image/gif");
        mimeTypeMap.put("jpeg", "image/jpeg");
        mimeTypeMap.put("png", "image/png");
    }

    /**
	 * @param shell
	 */
    private void loadDataFromServer(Shell shell) {
        final ContentSource contentSource = item.getContentSource();
        String taskName = MessageFormat.format(Messages.getString("BinaryControl.18"), new Object[] { name });
        ProgressBarUtils.executeActionWithProgress(shell, taskName, new AbstractServerAction(contentSource.getHost()) {

            public void run() throws Exception {
                List<RemoteContentItemProperty> itemProperties = contentSource.getContentServiceProvider().loadItemProperties(contentSource.getId(), item.getRemoteItem(), Collections.singletonList(name));
                if (itemProperties.size() > 0) {
                    value = ((BinaryProperty) itemProperties.get(0)).getValue();
                }
            }
        });
    }

    @Override
    public void setRemoteContentItemProperty(RemoteContentItemProperty property) {
        binaryProperty = (BinaryProperty) property;
        value = binaryProperty.getValue();
    }

    @Override
    public RemoteContentItemProperty getRemoteContentItemProperty() {
        return binaryProperty;
    }

    @Override
    public RemoteContentItemProperty updatePropertyValue() {
        if (binaryProperty == null) {
            binaryProperty = new BinaryProperty(name);
        }
        binaryProperty.setValue(value);
        return binaryProperty;
    }

    /**
	 * @param image
	 * @param parent
	 */
    private void openImage(final Image image, Shell parent) {
        Shell shell = new Shell(parent);
        shell.setText(IMAGE_TITLE);
        int width = image.getImageData().width;
        int height = image.getImageData().height;
        Canvas canvas = new Canvas(shell, SWT.NONE);
        canvas.setSize(width, height);
        canvas.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                e.gc.drawImage(image, 0, 0);
            }
        });
        shell.pack();
        shell.open();
    }

    /**
	 * 
	 */
    private void resizeContentPane() {
        scrolledCompositeContent.setSize(scrolledCompositeContent.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
        scrolledComposite.layout();
    }

    /**
	 * @param widget
	 */
    private void disposeWidget(Widget widget) {
        if (widget != null && !widget.isDisposed()) {
            widget.dispose();
        }
    }

    public String getGuessedMimeType() {
        return guessedMimeType;
    }
}
