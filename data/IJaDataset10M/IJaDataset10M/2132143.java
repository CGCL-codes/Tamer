package jweslley.ProblemSetVolumes.VolumeIV;

import java.io.IOException;
import jweslley.Problem;
import jweslley.Problem.Status;

/**
 * http://icpcres.ecs.baylor.edu/onlinejudge/external/4/445.html
 *
 * @author  Jonhnny Weslley
 * @version 1.00, 23/10/2008
 */
@Problem(Status.Accepted)
public class MarvelousMazes {

    public static void main(String[] args) throws IOException {
        StringBuilder out = new StringBuilder();
        int times = 0;
        int read;
        char c;
        while ((read = System.in.read()) != -1) {
            c = (char) read;
            if (Character.isDigit(c)) {
                times += c - '0';
            } else {
                if (c == '\n' || c == '!') {
                    out.append('\n');
                } else {
                    if (c == 'b') {
                        c = ' ';
                    }
                    for (int i = 0; i < times; i++) {
                        out.append(c);
                    }
                    times = 0;
                }
            }
        }
        System.out.print(out);
    }
}
