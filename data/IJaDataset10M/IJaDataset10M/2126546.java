package dryven.model.di;

import java.lang.annotation.Annotation;
import dryven.request.controller.paramtransform.ActionParameterBindCandidate;
import dryven.request.controller.paramtransform.ActionParameterTransformFeedback;
import dryven.request.http.Request;
import dryven.request.http.Response;

public class DependencyInjectorBinder implements ActionParameterBindCandidate {

    private DependencyInjector di;

    public DependencyInjectorBinder(DependencyInjector di) {
        super();
        this.di = di;
    }

    @Override
    public void apply(Class<?> type, Annotation[] annotations, Object currentValue, Request request, Response response, ActionParameterTransformFeedback feedback) {
        feedback.setParameter(di.load(request, type, annotations));
    }

    @Override
    public boolean canApply(Class<?> type, Annotation[] annotations) {
        return di.appliesToType(type, annotations);
    }

    @Override
    public boolean isValueGenerator() {
        return true;
    }

    @Override
    public String serializeParameter(Object o) {
        return null;
    }
}
