package net.community.chest.ui.components.datetime;

/**
 * <P>Copyright as per GPLv2</P>
 * @param <C> Type of {@link SpinnerTimeControl} being used
 * @author Lyor G.
 * @since Nov 18, 2010 8:03:27 AM
 */
public abstract class OptionalTimeControl<C extends SpinnerTimeControl> extends AbstractOptionalSpinnerDateTimeControl<C> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7735018732811376787L;

    public OptionalTimeControl(Class<C> valsClass) {
        super(valsClass);
    }

    public static class DefaultOptionalTimeControl extends OptionalTimeControl<SpinnerTimeControl> {

        /**
		 * 
		 */
        private static final long serialVersionUID = 2217274242520296729L;

        public DefaultOptionalTimeControl() {
            super(SpinnerTimeControl.class);
        }

        @Override
        protected SpinnerTimeControl createSpinnerControl() {
            return new SpinnerTimeControl();
        }
    }
}
