package de.andreavicentini.magicphoto.ui.directory;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.magiclabs.basix.IRenderer;
import de.andreavicentini.magicphoto.domain.directory.IDirectory;
import de.andreavicentini.magicphoto.domain.layer.ILayer;
import de.andreavicentini.visions.Execution;
import de.andreavicentini.visions.Execution.IActivity;
import de.andreavicentini.visions.Execution.IContext;

interface DirectoryTree {

    class DirectoryContentProvider implements ITreeContentProvider {

        private ILayer layer;

        public DirectoryContentProvider(ILayer layer) {
            this.layer = layer;
        }

        public Object[] getChildren(Object parentElement) {
            return this.layer.listDirectories((IDirectory) parentElement);
        }

        public Object getParent(Object element) {
            return ((IDirectory) element).getParent();
        }

        public boolean hasChildren(Object element) {
            return this.layer.listDirectories((IDirectory) element).length > 0;
        }

        public Object[] getElements(Object inputElement) {
            return getChildren(inputElement);
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    class BytesRenderer implements IRenderer<Long> {

        private final long KILO = 1024L;

        private final long MEGA = KILO * KILO;

        private final long GIGA = MEGA * KILO;

        class Struct {

            String suffix;

            long threshold;

            public boolean matches(long value) {
                return value > this.threshold;
            }
        }

        Struct[] levels = new Struct[] { new Struct() {

            {
                this.suffix = "GByte";
                this.threshold = GIGA;
            }
        }, new Struct() {

            {
                this.suffix = "MByte";
                this.threshold = MEGA;
            }
        }, new Struct() {

            {
                this.suffix = "KByte";
                this.threshold = 1;
            }

            public boolean matches(long value) {
                return true;
            }
        } };

        class CheckThreshold implements IActivity<Long, String> {

            private final Struct struct;

            private final Format fmt = new DecimalFormat();

            public CheckThreshold(Struct struct) {
                this.struct = struct;
            }

            public String process(IContext<Long, String> ctx) throws Exception {
                if (!this.struct.matches(ctx.getValue())) return ctx.proceed();
                return this.fmt.format(new Long(ctx.getValue().longValue() / this.struct.threshold)) + " " + this.struct.suffix;
            }
        }

        public String process(Long object) {
            Execution<Long, String> e = new Execution<Long, String>().add(new CheckThreshold(new Struct() {

                {
                    this.suffix = "GByte";
                    this.threshold = GIGA;
                }
            })).add(new CheckThreshold(new Struct() {

                {
                    this.suffix = "MByte";
                    this.threshold = MEGA;
                }
            })).add(new CheckThreshold(new Struct() {

                {
                    this.suffix = "KByte";
                    this.threshold = KILO;
                }
            })).add(new CheckThreshold(new Struct() {

                {
                    this.suffix = "Byte";
                    this.threshold = 1;
                }

                public boolean matches(long arg0) {
                    return true;
                }
            }));
            return e.execute(object);
        }
    }

    class DLabelProvider extends LabelProvider {

        private final BytesRenderer renderer = new BytesRenderer();

        @Override
        public String getText(Object element) {
            IDirectory directory = (IDirectory) element;
            return MessageFormat.format("{0} [ {1} / {2} ]", directory.getPath().getName(), directory.getAttributes().pictureNumber, this.renderer.process(directory.getAttributes().size));
        }
    }
}
