package de.capacis.jzeemap;

import java.util.Arrays;
import de.capacis.jzeemap.transformation.Transformation;

public class TransformationFactory {

    public static final class TransformationException extends Exception {

        public TransformationException() {
            super();
        }

        public TransformationException(String message, Throwable cause) {
            super(message, cause);
        }

        public TransformationException(String message) {
            super(message);
        }

        public TransformationException(Throwable cause) {
            super(cause);
        }
    }

    private static enum Transformations implements Transformation {

        identity {

            @Override
            public SceneObject transform(SceneObject in) {
                return in;
            }
        }
        , xz {

            @Override
            public SceneObject transform(SceneObject in) {
                return new SceneObject(in.getX(), -in.getZ(), in.getTimestamp());
            }
        }
        , arithMedXY {

            @Override
            public SceneObject transform(SceneObject in) {
                return new SceneObject((in.getX() + in.getY()) / 2.0, -in.getZ(), in.getTimestamp());
            }
        }

    }

    public Transformation create(final String transformationId) throws TransformationException {
        try {
            final Transformation transformation = Transformations.valueOf(transformationId);
            return transformation;
        } catch (NullPointerException ex) {
            throw new TransformationException("could not parse transformation. Must be one of " + Arrays.toString(Transformations.values()), ex);
        } catch (IllegalArgumentException ex) {
            throw new TransformationException("invalid transformation: " + transformationId + ".  Must be one of " + Arrays.toString(Transformations.values()), ex);
        }
    }
}
