package example.spring.web.controller;

import java.util.Collection;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public final class ReferenceData06 {

    private final Collection<FormOption> formOptions;

    private final String refData1;

    private final String refData2;

    private final Collection<Integer> selected;

    public ReferenceData06(final Collection<FormOption> formOptions, final String refData1, final String refData2, final Collection<Integer> _selected) {
        super();
        this.formOptions = formOptions;
        this.refData1 = refData1;
        this.refData2 = refData2;
        this.selected = _selected;
    }

    public Collection<FormOption> getFormOptions() {
        return (formOptions);
    }

    public String getRefData1() {
        return (refData1);
    }

    public String getRefData2() {
        return (refData2);
    }

    /**
     * @return the selected
     */
    public Collection<Integer> getSelected() {
        return (selected);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
