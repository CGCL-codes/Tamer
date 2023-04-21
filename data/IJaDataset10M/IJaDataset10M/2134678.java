package sjtu.llgx.calGPA;

public class CalGPAActionImpl extends CalGPAAction {

    protected GPACalculator createCalculator(String calType) {
        try {
            return (GPACalculator) (Class.forName(calType).newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
