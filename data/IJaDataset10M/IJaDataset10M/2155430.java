package nl.flotsam.preon.descriptor;

import nl.flotsam.limbo.Expression;
import nl.flotsam.pecia.Contents;
import nl.flotsam.pecia.Documenter;
import nl.flotsam.pecia.ParaContents;
import nl.flotsam.pecia.SimpleContents;
import nl.flotsam.preon.CodecDescriptor;
import nl.flotsam.preon.Resolver;
import nl.flotsam.preon.CodecDescriptor.Adjective;
import nl.flotsam.preon.binding.Binding;
import nl.flotsam.preon.buffer.ByteOrder;
import nl.flotsam.preon.rendering.IdentifierRewriter;
import nl.flotsam.preon.util.DocumentParaContents;
import nl.flotsam.preon.util.TextUtils;

public class Documenters {

    private static final char[] HEX_SYMBOLS = "0123456789abcdef".toCharArray();

    public static Documenter<ParaContents<?>> forExpression(final Expression<?, Resolver> expr) {
        return new Documenter<ParaContents<?>>() {

            public void document(ParaContents<?> target) {
                if (expr == null) {
                    target.text("(unknown)");
                } else {
                    if (expr.isParameterized()) {
                        Expression<?, Resolver> simplified = expr.simplify();
                        simplified.document(new DocumentParaContents(target));
                    } else {
                        target.text(expr.eval(null).toString());
                    }
                }
            }
        };
    }

    public static Documenter<ParaContents<?>> forNumericValue(final int nrBits, final ByteOrder byteOrder) {
        return new Documenter<ParaContents<?>>() {

            public void document(ParaContents<?> target) {
                target.text(nrBits + "bits numeric value");
                if (nrBits > 8) {
                    target.text(" (");
                    switch(byteOrder) {
                        case BigEndian:
                            target.text("big endian");
                            break;
                        case LittleEndian:
                            target.text("little endian");
                            break;
                    }
                    target.text(")");
                }
            }
        };
    }

    public static Documenter<ParaContents<?>> forByteOrder(final ByteOrder byteOrder) {
        return new Documenter<ParaContents<?>>() {

            public void document(ParaContents<?> target) {
                switch(byteOrder) {
                    case BigEndian:
                        target.text("big endian");
                        break;
                    case LittleEndian:
                        target.text("little endian");
                        break;
                }
            }
        };
    }

    public static Documenter<ParaContents<?>> forBits(final Expression<Integer, Resolver> expr) {
        if (expr == null) {
            return new Documenter<ParaContents<?>>() {

                public void document(ParaContents<?> target) {
                    target.text("(unknown)");
                }
            };
        } else {
            if (expr.isParameterized()) {
                return forExpression(expr);
            } else {
                return new Documenter<ParaContents<?>>() {

                    public void document(ParaContents<?> target) {
                        int nrBits = expr.eval(null);
                        target.text(TextUtils.bitsToText(nrBits));
                    }
                };
            }
        }
    }

    public static Documenter<ParaContents<?>> forBindingName(final Binding binding, final IdentifierRewriter rewriter) {
        return new Documenter<ParaContents<?>>() {

            public void document(ParaContents<?> target) {
                target.term(binding.getId(), rewriter.rewrite(binding.getName()));
            }
        };
    }

    public static Documenter<SimpleContents<?>> forBindingDescription(final Binding binding) {
        return new Documenter<SimpleContents<?>>() {

            public void document(SimpleContents<?> target) {
                binding.describe(target);
            }
        };
    }

    public static Documenter<ParaContents<?>> forHexSequence(final byte[] sequence) {
        return new Documenter<ParaContents<?>>() {

            public void document(ParaContents<?> target) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < sequence.length; i++) {
                    if (i != 0) {
                        builder.append(' ');
                    }
                    builder.append(HEX_SYMBOLS[((sequence[i] >> 4) & 0xf)]);
                    builder.append(HEX_SYMBOLS[(sequence[i] & 0x0f)]);
                }
                target.text(builder.toString());
            }
        };
    }

    public static Documenter<ParaContents<?>> forDescriptor(final CodecDescriptor descriptor) {
        return new Documenter<ParaContents<?>>() {

            public void document(ParaContents<?> target) {
                descriptor.reference(Adjective.THE, false);
            }
        };
    }
}
