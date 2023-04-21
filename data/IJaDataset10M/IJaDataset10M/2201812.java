package com.gwtaf.core.client.actionadapter;

import com.gwtaf.core.client.editcontext.IEditContext;
import com.gwtaf.core.client.editcontext.IEditContext.State;
import com.gwtaf.core.shared.permission.Permission;

public interface IEditContextPermission {

    IEditContextPermission ALL = new IEditContextPermission() {

        @Override
        public Permission getPermission(IEditContext<?> ctx) {
            return Permission.ALLOWED;
        }
    };

    /**
   * if the context is handling a new model, permission will be denied, else allowed
   */
    IEditContextPermission NOTNEW = new IEditContextPermission() {

        @Override
        public Permission getPermission(IEditContext<?> ctx) {
            return ctx.isDataNull() || ctx.isNew() ? Permission.UNALLOWED : Permission.ALLOWED;
        }
    };

    IEditContextPermission EDIT = new IEditContextPermission() {

        @Override
        public Permission getPermission(IEditContext<?> ctx) {
            return !ctx.isDataNull() && ctx.getState() == State.EDIT ? Permission.ALLOWED : Permission.UNALLOWED;
        }
    };

    IEditContextPermission EDITNEW = new IEditContextPermission() {

        @Override
        public Permission getPermission(IEditContext<?> ctx) {
            return !ctx.isDataNull() && ctx.getState() == State.EDIT && ctx.isNew() ? Permission.ALLOWED : Permission.UNALLOWED;
        }
    };

    IEditContextPermission NOEDIT = new IEditContextPermission() {

        @Override
        public Permission getPermission(IEditContext<?> ctx) {
            return !ctx.isDataNull() && ctx.getState() == State.EDIT ? Permission.UNALLOWED : Permission.ALLOWED;
        }
    };

    IEditContextPermission ASCTX = new IEditContextPermission() {

        @Override
        public Permission getPermission(IEditContext<?> ctx) {
            return ctx.getPermission();
        }
    };

    Permission getPermission(IEditContext<?> ctx);
}
