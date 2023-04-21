package ru.yandex.strictweb.ajaxtools.exception;

import ru.yandex.strictweb.ajaxtools.annotation.AjaxTransient;
import ru.yandex.strictweb.ajaxtools.annotation.Presentable;

@Presentable
public class AjaxException extends RuntimeException {

    @AjaxTransient
    private static final long serialVersionUID = 3033599120667827345L;

    public AjaxException() {
        super("Common AJAX exception");
    }

    public AjaxException(Throwable e) {
        super(e);
    }

    public AjaxException(String message) {
        super(message);
    }

    public final String getType() {
        return this.getClass().getSimpleName();
    }

    public String getMessage() {
        return super.getMessage();
    }
}
