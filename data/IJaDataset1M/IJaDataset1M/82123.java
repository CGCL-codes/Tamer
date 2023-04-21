package pikes.peak;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import pikes.ecma.SourceElements;
import pikes.html.xhtml.Inline;
import pikes.html.xhtml.form.Option;
import pikes.html.xhtml.form.Select;
import org.springframework.web.servlet.support.RequestContext;

public class ComboBox<T> implements InlineComponent {

    private CharSequence name = null;

    private RequestProperty<Collection<T>> listProvider = null;

    private ObjectFormatter<T> formatter = ObjectFormatter.TOSTRINGFORMATTER;

    private RequestProperty<T> selectedObjectProvider = RequestProperty.NULL_VALUE_PROVIDER;

    private RequestProperty<SourceElements> onChange = null;

    public ComboBox() {
    }

    public ComboBox(CharSequence name) {
        this.name = name;
    }

    public ComboBox(CharSequence name, RequestProperty<Collection<T>> listProvider) {
        this.name = name;
        this.listProvider = listProvider;
    }

    public ComboBox(RequestProperty<Collection<T>> listProvider) {
        this.listProvider = listProvider;
    }

    public ComboBox(RequestProperty<Collection<T>> listProvider, ObjectFormatter<T> formatter) {
        this.listProvider = listProvider;
        this.formatter = formatter;
    }

    protected Select createEmptySelect(Map model, RequestContext requestContext) throws Exception {
        return new Select(new Option());
    }

    protected Option createOption(T object, Map model, RequestContext requestContext) throws Exception {
        return new Option(formatter.getValue(object, model, requestContext), formatter.getLabel(object, model, requestContext), object.equals(selectedObjectProvider.getValue(model, requestContext)));
    }

    public Inline createHtml(Map model, RequestContext requestContext) throws Exception {
        Select select = null;
        if (listProvider == null) {
            select = createEmptySelect(model, requestContext);
        } else {
            Collection<T> objects = listProvider.getValue(model, requestContext);
            if (objects == null) {
                select = createEmptySelect(model, requestContext);
            } else {
                if (objects.isEmpty()) {
                    select = createEmptySelect(model, requestContext);
                } else {
                    Iterator<T> iObjects = objects.iterator();
                    T first = iObjects.next();
                    select = new Select(createOption(first, model, requestContext));
                    while (iObjects.hasNext()) {
                        T next = iObjects.next();
                        select.add(createOption(next, model, requestContext));
                    }
                }
            }
        }
        if (name != null) {
            select.setName(name);
        }
        if (onChange != null) {
            select.setOnChange(onChange.getValue(model, requestContext));
        }
        return select;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public void setFormatter(ObjectFormatter<T> objectFormatter) {
        this.formatter = objectFormatter;
    }

    public void setSelectedObjectProvider(RequestProperty<T> valueProvider) {
        this.selectedObjectProvider = valueProvider;
    }

    public void setOnChange(RequestProperty<SourceElements> script) {
        this.onChange = script;
    }

    public void setOnChange(SourceElements script) {
        this.setOnChange(new RequestIndependentProperty<SourceElements>(script));
    }
}
