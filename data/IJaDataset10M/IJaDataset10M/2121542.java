package br.org.bertol.mestrado.engine.optimisation.pso;

/**
 * Representa um elemento da composição da velocidade.
 * @author contaqualquer
 * @param <T> Tipo de velocidade
 */
public class Velocity<T> {

    /***/
    private T par1;

    /***/
    private T par2;

    /**
     * Cria par de trocas de uma velocidade.
     * @param pr1
     *            posição de
     * @param pr2
     *            posiçã para
     */
    public Velocity(final T pr1, final T pr2) {
        this.par1 = pr1;
        this.par2 = pr2;
    }

    /**
     * @return Par 1
     */
    public final T getPar1() {
        return par1;
    }

    /**
     * @return Par 2
     */
    public final T getPar2() {
        return par2;
    }

    /**
     * Seta par 2.
     * @param par
     *            Elemento
     */
    public final void setPar2(final T par) {
        this.par2 = par;
    }

    /**
     * Seta par 1.
     * @param par
     *            Par 1
     */
    public final void setPar1(final T par) {
        this.par1 = par;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("[PAR : " + par1 + " - " + par2 + " ]");
        return buffer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((par1 == null) ? 0 : par1.hashCode());
        result = prime * result + ((par2 == null) ? 0 : par2.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Velocity<?>)) {
            return false;
        }
        Velocity<?> other = (Velocity<?>) obj;
        if (par1 == null) {
            if (other.par1 != null) {
                return false;
            }
        } else if (!par1.equals(other.par1)) {
            return false;
        }
        if (par2 == null) {
            if (other.par2 != null) {
                return false;
            }
        } else if (!par2.equals(other.par2)) {
            return false;
        }
        return true;
    }
}
