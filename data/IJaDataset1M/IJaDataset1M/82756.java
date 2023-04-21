package com.incendiaryblue.cmslite;

/**
 * A RuntimeException thrown when a piece of content is placed in a category
 * that will cause an illegal clash, usually caused by a content item of the
 * same name being present in the category.
 *
 * @author Giles Taylor, syzygy
 * @version 1.0
 */
public class ContentLocationException extends RuntimeException {

    public ContentLocationException() {
        super();
    }

    public ContentLocationException(String message) {
        super(message);
    }
}
