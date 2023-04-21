package org.ocallahan.chronomancer.typeRenderers;

import java.math.BigInteger;
import org.ocallahan.chronicle.Type;
import org.ocallahan.chronomancer.DataContext;
import org.ocallahan.chronomancer.IInteractiveFigure;
import org.ocallahan.chronomancer.IReceiver;
import org.ocallahan.chronomancer.ITypeRenderer;
import org.ocallahan.chronomancer.ITypeRendererFactory;
import org.ocallahan.chronomancer.RenderedData;
import org.ocallahan.chronomancer.State;
import org.ocallahan.chronomancer.StringFigure;
import org.ocallahan.chronomancer.UIDataSink;

public class IntRenderer implements ITypeRenderer {

    public String getName() {
        return "Integer";
    }

    public void renderType(final State state, RenderedData rd, final IReceiver<IInteractiveFigure> receiver) {
        final Type.Int tInt = (Type.Int) rd.getBareType();
        rd.getSource().read(0, tInt.getSize(), new UIDataSink(state) {

            public void receiveOnUIThread(byte[] data, boolean[] valid) {
                if (!RenderedData.allValid(valid)) {
                    receiver.receive(new InvalidDataFigure());
                    return;
                }
                data = state.getSession().getArchitecture().toBigEndian(data);
                if (!tInt.isSigned() && (data[0] & 0x80) != 0) {
                    byte[] newData = new byte[data.length + 1];
                    System.arraycopy(data, 0, newData, 1, data.length);
                    data = newData;
                }
                BigInteger val = new BigInteger(data);
                receiver.receive(new StringFigure(val.toString()));
            }
        });
    }

    private static IntRenderer renderer = new IntRenderer();

    public static class Factory extends ITypeRendererFactory.Stub {

        public ITypeRenderer getRendererFor(Type t, DataContext context) {
            if (!(t instanceof Type.Int)) return null;
            return renderer;
        }
    }
}
