package samples.annotationbased;

import samples.Service;

public class AnnotationDemo {

    private final Service service;

    public AnnotationDemo(Service service) {
        this.service = service;
    }

    public String getServiceMessage() {
        return service.getServiceMessage();
    }
}
