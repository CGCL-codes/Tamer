package vaani.internal.purple;

/**
 *
 * @author sourcemorph
 */
public class Result {

    private InstantMessageAccount account;

    private Buddy buddy;

    public Result(InstantMessageAccount account, Buddy buddy) {
        this.account = account;
        this.buddy = buddy;
    }

    public InstantMessageAccount getAccount() {
        return account;
    }

    public Buddy getBuddy() {
        return buddy;
    }
}
