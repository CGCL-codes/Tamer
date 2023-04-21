package net.sf.snver.pileup.util;

import static net.sf.snver.pileup.config.Dictionary.*;
import static net.sf.snver.pileup.config.Dictionary.TYPE_BOOLEAN;
import static net.sf.snver.pileup.config.Dictionary.TYPE_DOUBLE;

public class Usage {

    private static final String NULL = "NULL";

    public static String getIndividualUsage() {
        return "[SNVerIndividual Usage]\nUsage: java -jar SNVerIndividual.jar -i input_file -r reference_file [options]\n" + "Input file must be a standard bam file\n" + "\t-i\t<input file (required) >\n" + "\t-r\t<reference file (required) >\n" + "Options:\n" + "\t-l\t<target region bed file (default is null) >\n" + "\t-o\t<prefix output file (default is input_file name) >\n" + "\t-n\t<the number of haploids (default is 2) >\n" + "\t-het\t<heterozygosity (default is 0.001) >\n" + "\t-mq\t<mapping quality threshold (default is 20) >\n" + "\t-bq\t<base quality threshold (default is 17) >\n" + "\t-s\t<strand bias threshold (default is 0.0001) >\n" + "\t-f\t<fisher exact threshold (default is 0.0001) >\n" + "\t-p\t<p-value threshold (default is bonferroni=0.05) >\n" + "\t-a\t<at least how many reads supporting each strand for alternative allele (default is 1)>\n" + "\t-b\t<discard locus with ratio of alt/ref below the threshold (default is 0.25)>\n" + "\t-u\t<inactivate -s and -f above this threshold (default is 30)  >\n" + "\t-db\t<path for dbSNP, column number of chr, pos and snp_id (format: dbsnp,1,2,3; default null)>\n";
    }

    public static String getPoolUsage() {
        return "[SNVerPool Usage]\nUsage: java -jar SNVerPool.jar -i input_directory -r reference_file [-c pool_info_file | -n number_of_haploids] [options]\n" + "Input directory must be a directory contains a batch of standard bam files\n" + "\t-i\t<input directory (required) >\n" + "\t-r\t<reference file (required) >\n" + "\t-c\t<pool info file (preferred) >\n" + "\t-n\t<the number of haploids (required when option \"-c\" is not given)>\n" + "Options:\n" + "\t-l\t<target region bed file (default is null) >\n" + "\t-o\t<prefix output file (default is input_file name) >\n" + "\t-mq\t<mapping quality threshold (default is 20) >\n" + "\t-bq\t<base quality threshold (default is 17) >\n" + "\t-s\t<strand bias threshold (default is 0.0001) >\n" + "\t-f\t<fisher exact threshold (default is 0.0001) >\n" + "\t-p\t<p-value threshold (default is bonferroni=0.05) >\n" + "\t-a\t<at least how many reads supporting each strand for alternative allele (default is 1)>\n" + "\t-t\t<allele frequency threshold (default is 0)>\n" + "\t-u\t<inactivate -s and -f above this threshold (default is 30)  >\n" + "\t-db\t<path for dbSNP, column number of chr, pos and snp_id (format: dbsnp,1,2,3; default null)>\n";
    }

    public static boolean isValueValid(String value, int type) throws NoMatchOptionException, NumberFormatException {
        switch(type) {
            case TYPE_STRING:
                if (NULL.equals(value)) throw new NoMatchOptionException("Lack of required parameter");
            case TYPE_NULLABLE:
                return true;
            case TYPE_INTEGER:
                Integer.valueOf(value);
                return true;
            case TYPE_LONG:
                Long.valueOf(value);
                return true;
            case TYPE_DOUBLE:
                Double.valueOf(value);
                return true;
            case TYPE_BOOLEAN:
                Boolean.valueOf(value);
                return true;
            default:
                return false;
        }
    }
}
