package consciouscode.bonsai.components;

import java.awt.Color;
import java.text.Format;
import javax.swing.JLabel;
import consciouscode.bonsai.channels.Channel;
import consciouscode.bonsai.channels.ChannelEvent;
import consciouscode.bonsai.channels.ChannelListener;

/**
   A Swing label that dynamically changes its text based on a {@link Channel}.

   <p>
   The text that is displayed for a given value is determined by two properties
   on the BLabel: <code>format</code> and <code>nullText</code>. When the
   channel's value is <code>null</code>, the <code>nullText</code> is rendered.
   If a {@link Format} has been specified, it is used to convert any non-null
   value to a String, as with
   <pre>getFormat().format(value)</pre>
   Finally, if no format is set, then the value's <code>toString</code> method
   is called.

   <p>
   By default, the foreground color of a BLabel is black; as always, this
   can be changed via {@link #setForeground}.
*/
public class BLabel extends JLabel {

    public BLabel(Channel textChannel) {
        this(textChannel, null);
    }

    public BLabel(Channel textChannel, Format format) {
        myTextChannel = textChannel;
        myFormat = format;
        textChannel.addChannelListener(new ChannelListener() {

            public void channelUpdate(ChannelEvent event) {
                updateText();
            }
        });
        setForeground(Color.black);
        updateText();
    }

    /**
       Gets the text that will be rendered in this label when the channel's
       value is <code>null</code>.  The default value is a string containing
       a single space (which ensures that the label will have a useful
       initial height).
    */
    public String getNullText() {
        return myNullText;
    }

    public void setNullText(String text) {
        myNullText = text;
        updateText();
    }

    public Format getFormat() {
        return myFormat;
    }

    public void setFormat(Format format) {
        myFormat = format;
        updateText();
    }

    private void updateText() {
        String text = null;
        Object value = myTextChannel.getValue();
        if (value != null) {
            if (myFormat != null) {
                text = myFormat.format(value);
            } else {
                text = value.toString();
            }
        }
        if (text == null) {
            text = myNullText;
        }
        setText(text);
    }

    private Channel myTextChannel;

    private Format myFormat;

    private String myNullText = " ";
}
