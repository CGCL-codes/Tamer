package jweslley.ContestVolumes.VolumeCIX;

import java.util.Scanner;
import jweslley.Problem;
import jweslley.Problem.Status;

/**
 * http://icpcres.ecs.baylor.edu/onlinejudge/external/109/10931.html
 *
 * @author  Jonhnny Weslley
 * @version 1.00, 18/10/2008
 */
@Problem(Status.Accepted)
public class Parity {

    public static void main(String[] args) {
        StringBuilder out = new StringBuilder();
        Scanner in = new Scanner(System.in);
        long i;
        while (in.hasNext()) {
            i = in.nextLong();
            if (i == 0) {
                break;
            }
            String binary = Long.toBinaryString(i);
            out.append(String.format("The parity of %s is %s (mod 2).\n", binary, binary.replace("0", "").length()));
        }
        System.out.print(out);
    }
}
