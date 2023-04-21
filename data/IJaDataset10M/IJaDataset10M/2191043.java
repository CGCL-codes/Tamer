package mcaplantlr.runtime;

/** The most common stream of tokens where every token is buffered up
 *  and tokens are filtered for a certain channel (the parser will only
 *  see these tokens).
 *
 *  Even though it buffers all of the tokens, this token stream pulls tokens
 *  from the tokens source on demand. In other words, until you ask for a
 *  token using consume(), LT(), etc. the stream does not pull from the lexer.
 *
 *  The only difference between this stream and BufferedTokenStream superclass
 *  is that this stream knows how to ignore off channel tokens. There may be
 *  a performance advantage to using the superclass if you don't pass
 *  whitespace and comments etc. to the parser on a hidden channel (i.e.,
 *  you set $channel instead of calling skip() in lexer rules.)
 *
 *  @see org.antlr.runtime.UnbufferedTokenStream
 *  @see org.antlr.runtime.BufferedTokenStream
 */
public class CommonTokenStream extends BufferedTokenStream {

    /** Skip tokens on any channel but this one; this is how we skip whitespace... */
    protected int channel = Token.DEFAULT_CHANNEL;

    public CommonTokenStream() {
        ;
    }

    public CommonTokenStream(TokenSource tokenSource) {
        super(tokenSource);
    }

    public CommonTokenStream(TokenSource tokenSource, int channel) {
        this(tokenSource);
        this.channel = channel;
    }

    /** Always leave p on an on-channel token. */
    public void consume() {
        if (p == -1) setup();
        p++;
        sync(p);
        while (tokens.get(p).getChannel() != channel) {
            p++;
            sync(p);
        }
    }

    protected Token LB(int k) {
        if (k == 0 || (p - k) < 0) return null;
        int i = p;
        int n = 1;
        while (n <= k) {
            i = skipOffTokenChannelsReverse(i - 1);
            n++;
        }
        if (i < 0) return null;
        return tokens.get(i);
    }

    public Token LT(int k) {
        if (p == -1) setup();
        if (k == 0) return null;
        if (k < 0) return LB(-k);
        int i = p;
        int n = 1;
        while (n < k) {
            i = skipOffTokenChannels(i + 1);
            n++;
        }
        return tokens.get(i);
    }

    /** Given a starting index, return the index of the first on-channel
     *  token.
     */
    protected int skipOffTokenChannels(int i) {
        sync(i);
        while (tokens.get(i).getChannel() != channel) {
            i++;
            sync(i);
        }
        return i;
    }

    protected int skipOffTokenChannelsReverse(int i) {
        while (i >= 0 && ((Token) tokens.get(i)).getChannel() != channel) {
            i--;
        }
        return i;
    }

    protected void setup() {
        p = 0;
        sync(0);
        int i = 0;
        while (tokens.get(i).getChannel() != channel) {
            i++;
            sync(i);
        }
        p = i;
    }

    /** Reset this token stream by setting its token source. */
    public void setTokenSource(TokenSource tokenSource) {
        super.setTokenSource(tokenSource);
        channel = Token.DEFAULT_CHANNEL;
    }
}
