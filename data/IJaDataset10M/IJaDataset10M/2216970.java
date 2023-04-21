package emil.poker.entities;

import java.io.Serializable;

@Deprecated
public class WinOrDrawAgainstNumber extends FieldsForStats implements Serializable {

    private long num_of_opponents;

    public long getNum_of_opponents() {
        return num_of_opponents;
    }

    public void setNum_of_opponents(long num_of_opponents) {
        this.num_of_opponents = num_of_opponents;
    }
}
