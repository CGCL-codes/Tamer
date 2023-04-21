package com.webkreator.qlue.router;

import com.webkreator.qlue.TransactionContext;
import com.webkreator.qlue.view.StatusCodeView;

/**
 * Routes transaction to a view that
 * responds with a custom error code.
 */
public class StatusCodeRouter implements Router {

    private int status;

    private String message;

    public StatusCodeRouter(int status) {
        this.status = status;
    }

    public StatusCodeRouter(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public Object route(TransactionContext context, String extraPath) {
        return new StatusCodeView(status, message);
    }
}
