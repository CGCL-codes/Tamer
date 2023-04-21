package edu.rabbit.kernel.schema;

import org.antlr.runtime.tree.CommonTree;
import edu.rabbit.schema.IBlobLiteral;

/**
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 */
public class BlobLiteral extends Expression implements IBlobLiteral {

    private final byte[] value;

    public BlobLiteral(CommonTree ast) {
        assert "blob_literal".equalsIgnoreCase(ast.getText());
        this.value = parseBlob(ast.getChild(0).getText());
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return asBlob(value);
    }

    public static byte[] parseBlob(String data) {
        data = data.substring(2, data.length() - 1).toLowerCase();
        byte[] buffer = new byte[data.length() / 2];
        for (int i = 0; i < buffer.length; i++) {
            char c1 = data.charAt(i * 2);
            char c2 = data.charAt(i * 2 + 1);
            int b1 = c1 - ((c1 >= 'a') ? 'a' : '0');
            int b2 = c2 - ((c2 >= 'a') ? 'a' : '0');
            buffer[i] = (byte) (b1 * 16 + b2);
        }
        return buffer;
    }

    public static String asBlob(byte[] data) {
        StringBuffer buffer = new StringBuffer("x'");
        for (byte b : data) {
            buffer.append((char) (b / 16 > 9 ? 'a' + b / 16 - 10 : '0' + b / 16));
            buffer.append((char) (b % 16 > 9 ? 'a' + b % 16 - 10 : '0' + b % 16));
        }
        buffer.append("'");
        return buffer.toString();
    }
}
