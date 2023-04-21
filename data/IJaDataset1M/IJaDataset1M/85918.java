package org.sam.interpoladores;

/**
 * Clase contenedora con las implementaciones de un <i>GetterGroup</i>, tanto en precisión {@code double},
 * {@code float}, como {@code int}.
 * <p>Este <i>Getter</i> permite agrupar varios <i>Getter</i>s de forma secuencial.</p>
 */
public final class GetterGroup {

    private GetterGroup() {
    }

    /**
     * Clase que implementa un {@code GetterGroup} con precisión {@code double}.
     * @param <T> Tipo genérico de datos empleados.
     */
    public static final class Double<T> implements Getter.Double<T> {

        private final double[] keys;

        private final Getter.Double<T> getters[];

        /**
         * Constructor que genera un {@code GetterGroup} con precisión {@code double}.
         * @param keys claves asociadas a los {@code Getter}s.
         * @param getters {@code Getter}s que contienen los valores que serán devueltos.
         */
        public Double(double keys[], Getter.Double<T>[] getters) {
            assert (keys.length == getters.length);
            this.keys = keys;
            this.getters = getters;
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public T get(double key) {
            int index = Keys.findIndexKey(key, keys);
            double subkey = key;
            if (index < 0) return getters[0].get(subkey);
            if ((index == getters.length - 1) || (key - keys[index] < keys[index + 1] - key)) getters[index].get(subkey);
            return getters[index + 1].get(subkey);
        }
    }

    /**
     * Clase que implementa un {@code GetterGroup} con precisión {@code float}.
     * @param <T> Tipo genérico de datos empleados.
     */
    public static final class Float<T> implements Getter.Float<T> {

        private final float[] keys;

        private final Getter.Float<T> getters[];

        /**
         * Constructor que genera un {@code GetterGroup} con precisión {@code float}.
         * @param keys claves asociadas a los {@code Getter}s.
         * @param getters {@code Getter}s que contienen los valores que serán devueltos.
         */
        public Float(float keys[], Getter.Float<T>[] getters) {
            assert (keys.length == getters.length);
            this.keys = keys;
            this.getters = getters;
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public T get(float key) {
            int index = Keys.findIndexKey(key, keys);
            float subkey = key;
            if (index < 0) return getters[0].get(subkey);
            if ((index == getters.length - 1) || (key - keys[index] < keys[index + 1] - key)) getters[index].get(subkey);
            return getters[index + 1].get(subkey);
        }
    }

    /**
     * Clase que implementa un {@code GetterGroup} con precisión {@code int}.
     * @param <T> Tipo genérico de datos empleados.
     */
    public static final class Integer<T> implements Getter.Integer<T> {

        private final int[] keys;

        private final Getter.Integer<T> getters[];

        /**
         * Constructor que genera un {@code GetterGroup} con precisión {@code int}.
         * @param keys claves asociadas a los {@code Getter}s.
         * @param getters {@code Getter}s que contienen los valores que serán devueltos.
         */
        public Integer(int keys[], Getter.Integer<T>[] getters) {
            assert (keys.length == getters.length);
            this.keys = keys;
            this.getters = getters;
        }

        /**
		 * {@inheritDoc}
		 */
        @Override
        public T get(int key) {
            int index = Keys.findIndexKey(key, keys);
            int subkey = key;
            if (index < 0) return getters[0].get(subkey);
            if ((index == getters.length - 1) || (key - keys[index] < keys[index + 1] - key)) getters[index].get(subkey);
            return getters[index + 1].get(subkey);
        }
    }
}
