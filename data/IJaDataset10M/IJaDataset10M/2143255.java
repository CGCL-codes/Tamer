package freja.di.config.empty;

import freja.di.Binder;

/**
 * �����s��Ȃ� Binder �I�u�W�F�N�g�ł��B
 * @author SiroKuro
 */
public class EmptyBinder implements Binder {

    /**
	 * EmptyBinder �̃C���X�^���X�ł��B
	 */
    public static final EmptyBinder INSTANCE = new EmptyBinder();

    protected EmptyBinder() {
        ;
    }

    /**
	 * �����s���܂���B
	 */
    @Override
    public void bind(Object object) {
        ;
    }
}
