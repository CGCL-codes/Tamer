package osteching.sca.binding.blazeds.impl;

import osteching.sca.binding.blazeds.BlazeDSBinding;
import osteching.sca.binding.blazeds.BlazeDSBindingFactory;

/**
 * @author julian0zzx@gmail.com
 */
public class BlazeDSBindingFactoryImpl implements BlazeDSBindingFactory {

    @Override
    public BlazeDSBinding createBlazeDSBinding() {
        return new BlazeDSBindingImpl();
    }
}
