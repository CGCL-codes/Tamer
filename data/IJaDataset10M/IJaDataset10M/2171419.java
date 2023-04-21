package jweslley.ContestVolumes.VolumeCIII;

import java.util.Scanner;
import jweslley.Problem;
import jweslley.Problem.Status;

/**
 * http://icpcres.ecs.baylor.edu/onlinejudge/external/103/10327.html
 *
 * @author  Jonhnny Weslley
 * @version 1.00, 21/10/2008
 */
@Problem(Status.Accepted)
public class FlipSort {

    public static void main(String[] args) {
        StringBuilder out = new StringBuilder();
        Scanner in = new Scanner(System.in);
        int n, flips;
        int[] numbers = new int[1000];
        while (in.hasNext()) {
            n = in.nextInt();
            for (int i = 0; i < n; i++) {
                numbers[i] = in.nextInt();
            }
            flips = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    if (numbers[i] < numbers[j]) {
                        flips++;
                    }
                }
            }
            out.append("Minimum exchange operations : ").append(flips).append('\n');
        }
        System.out.print(out);
    }
}
