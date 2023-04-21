package uk.ac.bath.domains;

import java.util.Vector;
import uk.ac.bath.machine.bool.LogicalMachineFactory;
import uk.ac.bath.domains.mover.MrkINeuralProvider;
import uk.ac.bath.machine.neural.NeuralMachineFactory;

public class EnvironmentFactory {

    private static Vector<Object> providers = new Vector<Object>();

    static {
        providers.add(new SaneSequenceProvider());
        providers.add(new SaneSequenceProviderX());
        providers.add(new MrkIProvider());
        providers.add(new MrkINeuralProvider());
        providers.add(new PreyProvider(new LogicalMachineFactory(), "PreyLogic"));
        providers.add(new PreyProvider(new NeuralMachineFactory(), "PreyNeural"));
        providers.add(new SanePoleProvider("SanePole"));
        providers.add(new SanePoleProviderQuad("SanePoleQ"));
        providers.add(new SanePoleProviderVert("SanePoleQVert"));
        providers.add(new RandomPoleProviderVert("RandomPoleQVert"));
        providers.add(new VinillaRandomPoleProviderVert("VinillaRandomPoleQVert"));
        providers.add(new RandomPoleProvider("RandomPole"));
        providers.add(new SanePreyProvider(false, "SanePrey"));
        providers.add(new SanePreyProvider(true, "SanePreyR"));
    }

    public static Vector<Object> getOptions() {
        return providers;
    }
}
