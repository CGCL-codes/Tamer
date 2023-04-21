package edu.oasis.domain;

import edu.webteach.teach.bean.*;

public class CourseItem extends FileItem {

    protected Course source;

    CourseItem(Object location) {
        super(location);
        this.name = source.getTitle();
    }

    CourseItem(String name, Course source) {
        super(name);
        setSource(source);
    }

    public void save() {
    }

    public void load(Object location) {
        setLocation(location);
        CourseParser cp = new CourseParser(this.location);
        this.source = cp.getCourse();
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        if ((source != null) && (source != this.source) && (source instanceof Course)) {
            this.source = (Course) source;
            change();
        }
    }
}
