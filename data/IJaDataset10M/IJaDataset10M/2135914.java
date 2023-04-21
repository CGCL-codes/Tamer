package co.fxl.gui.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IDialog;

class AndroidDialog implements IDialog {

    private final class DialogButtonImpl implements IDialogButton {

        private static final String NO = "No";

        private static final String OK = "Ok";

        private static final String YES = "Yes";

        private boolean positive = true;

        private String text = YES;

        @Override
        public IDialogButton imageResource(String imageResource) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IDialogButton text(String text) {
            this.text = text;
            return this;
        }

        @Override
        public IDialogButton ok() {
            positive = true;
            return text(OK);
        }

        @Override
        public IDialogButton yes() {
            positive = true;
            return text(YES);
        }

        @Override
        public IDialogButton no() {
            positive = false;
            return text(NO);
        }

        @Override
        public IDialogButton cancel() {
            throw new UnsupportedOperationException();
        }

        @Override
        public IDialogButton addClickListener(final IClickListener l) {
            if (positive) {
                builder.setPositiveButton(text, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        l.onClick();
                    }
                });
            } else {
                builder.setNegativeButton(text, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        l.onClick();
                    }
                });
            }
            return this;
        }

        @Override
        public IDialogButton close() {
            throw new UnsupportedOperationException();
        }

        @Override
        public IDialogButton clickable(boolean clickable) {
            throw new UnsupportedOperationException();
        }
    }

    private AlertDialog.Builder builder;

    private AlertDialog dialog;

    AndroidDialog(AndroidDisplay androidDisplay) {
        builder = new AlertDialog.Builder(androidDisplay.activity).setCancelable(false);
    }

    @Override
    public IDialog modal(boolean modal) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IDialog title(String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IType message(final String message) {
        builder.setMessage(message);
        return new IType() {

            private IDialog text(String string) {
                builder.setMessage(string + ": " + message);
                return AndroidDialog.this;
            }

            @Override
            public IDialog info() {
                return text("INFO");
            }

            @Override
            public IDialog warn() {
                return text("WARNING");
            }

            @Override
            public IDialog error() {
                return text("ERROR");
            }
        };
    }

    @Override
    public IDialogButton addButton() {
        return new DialogButtonImpl();
    }

    @Override
    public IContainer container() {
        throw new UnsupportedOperationException();
    }

    @Override
    public IDialog visible(boolean visible) {
        if (visible) {
            dialog = builder.show();
        } else {
            dialog.cancel();
        }
        return this;
    }

    @Override
    public IDialog confirm() {
        throw new UnsupportedOperationException();
    }

    @Override
    public IDialog size(int width, int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IDialog width(int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IDialog height(int height) {
        throw new UnsupportedOperationException();
    }
}
