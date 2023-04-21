package gameditor.util;

/**
   class description
    
   @author George El-Haddad
   
   
   @see gameditor.util.StringUtil
   @see gameditor.util.TokenizeException
 */
public class ValueTokenizer {

    private String target;

    private String delim;

    private int valCount;

    private int total;

    private int next;

    private int x;

    private int y;

    /**
	   constructor default
	   
	   Does nothing.
	*/
    public ValueTokenizer() {
    }

    /**
	   constructor (String , String)
	 
	   Initializes all the variables in the ValueTokenizer
	   
	   @see gameditor.util.StringUtil
	   
	   @param String str
	   @param Stirng val
	*/
    public ValueTokenizer(String str, String val) {
        target = str;
        delim = val;
        total = StringUtil.countInstanceOf(str, val);
        valCount = 0;
        next = 0;
        x = 0;
        y = 0;
    }

    /**
	   String nextToken()
	   
	   Method for getting the next token in the string.
	   Will use an algorithm to extract a substring
	   using the given delimeter as the starting and
	   ending position to use as indicies.
	   
	   valCount is incremented with 2 because we expect
	   that each token is encapsulated with 2 delimeters.
	   So in essence, we actually are counting 2 values
	   at a time for 1 token.
	
	   @throws gameditor.util.TokenizeException
	*/
    public String nextToken() throws TokenizeException {
        String tmpWork = "";
        x = target.indexOf(delim, next);
        y = target.indexOf(delim, x + 1);
        if (x != -1 && y != -1) {
            tmpWork = target.substring(x + 1, y);
            next = y + 1;
            valCount += 2;
            return tmpWork;
        } else {
            throw new TokenizeException("Missing Delimiters");
        }
    }

    /**
	   int countTokens()
	   
	   Returns the total number of tokens by assuming
	   1 thing. That each token is encapsulated by 2
	   delimeters. So in essence if we have a total
	   of 6 instances of delim. We really have 3 tokens
	   as each token starts and ends with a delim.
	
	   @return int
	*/
    public int countTokens() {
        return total / 2;
    }

    /**
	   boolean hasMoreTokens()
	   
	   Returns true if there are more tokens
	   present to be tokenized in the string.
	   
	   @return boolean
	*/
    public boolean hasMoreTokens() {
        return total != valCount;
    }

    /**
	   static void main(String[])
	   
	   Used for external testing.
	   
	   @param String[] args
	*/
    public static void main(String[] args) {
        String str1 = "cars brands=\"mazda\" brands=\"BMW Z5\" brands=\"Mercedes\" /";
        System.out.println("Sending : " + str1);
        System.out.println("Tokenize for : '\"' value\n");
        try {
            ValueTokenizer vt = new ValueTokenizer(str1, "\"");
            while (vt.hasMoreTokens()) {
                System.out.println("token : " + vt.nextToken());
            }
        } catch (TokenizeException ex) {
            ex.printStackTrace();
        }
        System.out.println("\n--- End of Tests ---\n");
    }
}
