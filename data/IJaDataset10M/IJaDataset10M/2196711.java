package commandline;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import lzma.PostCompression;
import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;
import backbonecompression.BackboneEncoder;
import backbonecompression.Decoder;

public class CommandLine {

    public static final String fileTypeName = ".cbdd";

    /**
	 * This is the main method for the command line client
	 * It takes 1 or 2 filenames as input
	 * The first file is the input, if its a .bdd file it is encoded, if its a .cbdd file its decoded.
	 * The second filename allows for choosing another output file than the default which is the base name of the input file.
	 * Ie. if given arguments "pc.bdd" it will output "pc.cbdd" unless given another output filename.
	 * @param args
	 */
    public static void main(String[] args) throws IOException {
        if (args[0].endsWith(".bdd")) {
            System.out.println("Starting encode");
            BackboneEncoder be = new BackboneEncoder(false);
            String basename = args[0].substring(0, args[0].lastIndexOf("."));
            String tempName = basename + ".dat";
            String finalName = basename + fileTypeName;
            if (args.length == 2) {
                finalName = args[1];
            }
            System.out.println("Input file : " + args[0]);
            System.out.println("Output file : " + finalName);
            BDDFactory f = JFactory.init(1000000, 10000);
            BDD root = f.load(args[0]);
            be.Encode(new DataOutputStream(new FileOutputStream(tempName)), root);
            PostCompression.CompressFile(tempName, finalName);
            new File(tempName).delete();
            System.out.println("Finished encode");
        } else if (args[0].endsWith(".cbdd")) {
            System.out.println("Doing decode");
            String basename = args[0].substring(0, args[0].lastIndexOf("."));
            String tempName = basename + ".dat";
            String finalName = basename + ".bdd";
            if (args.length == 2) {
                finalName = args[1];
            }
            System.out.println("Input file : " + args[0]);
            System.out.println("Output file : " + finalName);
            if (!PostCompression.DecompressFile(args[0], tempName)) {
                System.out.println("Failed to perform LZMA decompression, cannot continue to decode BDD");
                return;
            }
            Decoder de = new Decoder(8);
            de.decode(new DataInputStream(new FileInputStream(tempName)), new PrintStream(finalName));
            new File(tempName).delete();
            System.out.println("Finished decoding");
        }
    }
}
