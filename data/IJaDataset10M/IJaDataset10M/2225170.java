package org.avm.sure.web.bean.form.snippet;

import org.avm.sure.domain.Question;
import org.avm.sure.web.bean.form.AbstractBeanForm;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public abstract class AbstractQuestionForm<T extends Question> extends AbstractBeanForm<Question> {

    private static final long serialVersionUID = 86334273831877943L;

    @Autowired
    private QuestionFieldFactory<T> formFieldFactory;

    public AbstractQuestionForm() {
        super();
        setFormFieldFactory(formFieldFactory);
    }
}
