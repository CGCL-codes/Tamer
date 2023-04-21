package com.directmodelling.android.binding;

import android.view.View;
import android.view.View.OnClickListener;
import com.directmodelling.api.Value.Mutable;
import com.directmodelling.impl.Command;
import com.directmodelling.impl.Variable;

public class ClickBinder extends Binder<View, Void, Void> implements OnClickListener {

    private static final Mutable<Void> dummy = new Variable<Void>() {

        @Override
        public com.directmodelling.api.Value.Type type() {
            return Type.tObject;
        }
    };

    private final Command command;

    public ClickBinder(Command m, View upgradeButton) {
        super(m, upgradeButton);
        this.command = m;
        upgradeButton.setOnClickListener(this);
    }

    @Override
    protected void setViewEnabled(boolean b) {
        view.setEnabled(b);
    }

    @Override
    protected void setViewValue(Void v, boolean force) {
    }

    @Override
    protected Void getViewValue() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (command.status().enabled) command.run();
    }
}
