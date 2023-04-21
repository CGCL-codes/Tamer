package jweslley.ProblemSetVolumes.VolumeIV;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import jweslley.Problem;
import jweslley.Problem.Status;

/**
 * http://icpcres.ecs.baylor.edu/onlinejudge/external/4/499.html
 * 
 * @author Jonhnny Weslley
 * @version 1.00, 23/10/2008
 */
@Problem(Status.Accepted)
public class WhatIsTheFrequencyKenneth {

    public static void main(String[] args) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int max;
        char c;
        String line;
        int[] frequency = new int[52];
        while ((line = in.readLine()) != null) {
            max = 0;
            for (int i = 0; i < 52; i++) {
                frequency[i] = 0;
            }
            for (int i = 0; i < line.length(); i++) {
                c = line.charAt(i);
                if ('A' <= c && c <= 'Z' && ++frequency[c - 'A'] > max) {
                    max = frequency[c - 'A'];
                }
                if ('a' <= c && c <= 'z' && ++frequency[c - 'a' + 26] > max) {
                    max = frequency[c - 'a' + 26];
                }
            }
            for (int i = 0; i < 26; i++) {
                if (frequency[i] == max) out.append((char) (i + 'A'));
            }
            for (int i = 26; i < 52; i++) {
                if (frequency[i] == max) out.append((char) (i + 'a' - 26));
            }
            out.append(' ').append(max).append('\n');
        }
        System.out.print(out);
    }
}
