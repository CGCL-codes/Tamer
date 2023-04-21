package adnotatio.client.annotator;

import adnotatio.client.annotator.model.Annotation;
import adnotatio.client.annotator.model.AnnotationModel;
import adnotatio.client.annotator.model.OnEditEvent;
import adnotatio.common.data.IPropertiesContainer;
import adnotatio.common.event.Event;
import adnotatio.common.event.EventListener;
import adnotatio.common.event.IEventListener;
import adnotatio.common.util.FieldPanelUtil;
import adnotatio.renderer.templates.FieldPanel;
import adnotatio.renderer.templates.TemplateBuilder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is an annotation form panel which draws itself using a template
 * corresponding to the "annotationForm" key. This panel catches the
 * {@link OnEditEvent} events fired by the model. It automatically starts the
 * editing process for the annotation defined in the {@link OnEditEvent#BEGIN}
 * event and it updates the content of this annotation when the
 * {@link OnEditEvent#SAVE} event is fired. The {@link OnEditEvent#SAVE} and
 * {@link OnEditEvent#CANCEL} events hide this panel.
 * 
 * @author kotelnikov
 */
class AnnotationForm extends Composite {

    /**
     * The name of the template used to render this panel
     */
    public static final String ANNOTATION_FORM = "annotationForm";

    /**
     * The edited annotation; The content of this annotation is used to fill the
     * editing form and it is updated when the {@link OnEditEvent#SAVE} event is
     * fired
     */
    private Annotation fCurrentAnnotation;

    /**
     * The form panel; the layout of this panel is defined a template with the
     * "annotationForm" key
     */
    private FieldPanel fForm;

    /**
     * The listener used to catch {@link OnEditEvent} events and start/stop the
     * editing process.
     */
    private IEventListener fListener = new EventListener() {

        /**
         * @see adnotatio.common.event.EventListener#handleEvent(adnotatio.common.event.Event)
         */
        public void handleEvent(Event event) {
            if (fForm == null) return;
            OnEditEvent e = (OnEditEvent) event;
            Annotation annotation = e.getAnnotation();
            if (fCurrentAnnotation == null) {
                if (e.stage == OnEditEvent.BEGIN) {
                    fCurrentAnnotation = annotation;
                    FieldPanelUtil.setFieldValues(fForm, fCurrentAnnotation.getProperties());
                    fForm.setEnabled(true);
                    fForm.setVisible(true);
                }
            } else if (fCurrentAnnotation.equals(annotation)) {
                switch(e.stage) {
                    case OnEditEvent.SAVE:
                    case OnEditEvent.CANCEL:
                        fForm.reset();
                        fForm.setEnabled(false);
                        fForm.setVisible(false);
                        fCurrentAnnotation = null;
                        break;
                }
            } else {
                fCurrentAnnotation.updateValues(annotation);
                FieldPanelUtil.updateFieldValues(fForm, fCurrentAnnotation.getProperties());
            }
        }

        /**
         * @see adnotatio.common.event.EventListener#prepareEvent(adnotatio.common.event.Event)
         */
        public boolean prepareEvent(Event event) {
            OnEditEvent e = (OnEditEvent) event;
            if (fCurrentAnnotation != null && OnEditEvent.SAVE == e.stage) {
                IPropertiesContainer properties = FieldPanelUtil.getFieldValues(fForm);
                fCurrentAnnotation.setProperties(properties);
            }
            return true;
        }
    };

    /**
     * The model used to catch events
     */
    private AnnotationModel fModel;

    /**
     * The main panel containing the form ({@link #fForm}) defined by the
     * template. This panel is required because the form does not exist before
     * the {@link #setFormTemplate(TemplateBuilder)} method is called
     */
    private FlowPanel fPanel = new FlowPanel();

    /**
     * The main constructor.
     * 
     * @param model the annotation model
     */
    public AnnotationForm(AnnotationModel model) {
        fModel = model;
        initWidget(fPanel);
    }

    /**
     * Creates and returns a new click listener used to fire a specific event
     * type
     * 
     * @param eventType the type of the edit event fired by this click listener
     * @return a new click listener firing a specific edit event
     */
    private ClickListener newClickListener(final int eventType) {
        return new ClickListener() {

            public void onClick(Widget sender) {
                if (fCurrentAnnotation != null) {
                    fModel.fireEvent(new OnEditEvent(fCurrentAnnotation, eventType));
                }
            }
        };
    }

    /**
     * This method adds the internal listener for edit events to the model
     * 
     * @see com.google.gwt.user.client.ui.Widget#onLoad()
     */
    protected void onLoad() {
        fModel.addListener(OnEditEvent.class, fListener);
    }

    /**
     * This method removes the internal edit listener from the list of model
     * listeners.
     * 
     * @see com.google.gwt.user.client.ui.Widget#onUnload()
     */
    protected void onUnload() {
        fModel.removeListener(OnEditEvent.class, fListener);
    }

    /**
     * Enables/disables the internal form.
     * 
     * @param enabled if this flag is <code>true</code> then the form will be
     *        enabled
     */
    public void setEnabled(boolean enabled) {
        if (fForm != null) {
            fForm.setEnabled(enabled);
        }
    }

    /**
     * This method is used for late binding of this form with the dynamic
     * template defining the layout of the editing form. When the form is
     * created then this method adds specific click listeners when the form is
     * cancelled or submitted. When the form is submitted then this panel fires
     * the {@link OnEditEvent#SAVE} event. Canceling fires the
     * {@link OnEditEvent#CANCEL} event.
     * 
     * @param builder the template builder used to create the editing form
     */
    public void setFormTemplate(TemplateBuilder builder) {
        fForm = builder.buildPanel(ANNOTATION_FORM);
        fPanel.add(fForm);
        fForm.setEnabled(false);
        fForm.setVisible(false);
        fForm.addSubmitListener(newClickListener(OnEditEvent.SAVE));
        fForm.addCancelListener(newClickListener(OnEditEvent.CANCEL));
    }
}
