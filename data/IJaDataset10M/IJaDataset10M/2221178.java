package lo.local.dreamrec.logic;

/**
 *
 */
public class FirstDerivatieveAbsFilter extends AbstractFilter {

    public FirstDerivatieveAbsFilter() {
        super(1);
    }

    @Override
    protected Data calculateOutputData() {
        if (inputBuffer.size() < 2) {
            return (new Data(0, inputDataCounter));
        }
        int dif = inputBuffer.get(1).getValue() - inputBuffer.get(0).getValue();
        inputBuffer.remove(0);
        return new Data(Math.abs(dif), inputDataCounter);
    }
}
