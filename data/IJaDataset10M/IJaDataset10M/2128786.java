package atm.jcsp;

import atm.spec.*;
import jcsp.lang.*;
import java.util.logging.Logger;

public class TellerMachine implements ATMSpec, CSProcess {

    protected CSProcess main;

    private final ChannelOutput c_ready;

    private final ChannelOutput c_debit;

    private final Channel c_ident, c_auth;

    private final AltingChannelInput c_setup;

    private final AltingChannelInput c_result;

    private final AltingChannelInput c_insert;

    private final AltingChannelInput c_pin;

    private final AltingChannelInput c_amount;

    private final ChannelOutput c_idle;

    private final ChannelOutput c_money;

    private final ChannelOutput c_eject;

    private BankSpec myBank;

    private PARSpec parameters;

    private IDSpec theID;

    private PINSpec thePin;

    private SUMSpec theAmount;

    private Boolean returnCard;

    public BankSpec ident(ATMSpec tid) {
        c_ident.write(tid);
        myBank = (BankSpec) c_ident.read();
        return myBank;
    }

    public void setup(PARSpec params) {
        parameters = params;
    }

    public void insert(IDSpec id) {
        theID = id;
    }

    public void pin(PINSpec pin) {
        thePin = pin;
    }

    public void amount(SUMSpec amount) {
        theAmount = amount;
    }

    public void result(Boolean ok) {
        returnCard = ok;
    }

    public void ready() {
        Logger.global.fine("ATM sends ready " + this);
        c_ready.write(null);
    }

    public Boolean auth(IDSpec id, PINSpec pin) {
        c_auth.write(id);
        c_auth.write(pin);
        returnCard = (Boolean) c_auth.read();
        return returnCard;
    }

    public void debit(ATMSpec from, IDSpec id, SUMSpec amt) {
        c_debit.write(from);
        c_debit.write(id);
        c_debit.write(amt);
    }

    public void idle(ATMSpec from) {
        c_idle.write(from);
    }

    public void money(ATMSpec from, SUMSpec amt) {
        c_money.write(from);
        c_money.write(amt);
    }

    public void eject(ATMSpec from) {
        c_eject.write(from);
    }

    public void run() {
        main.run();
    }

    private void init() {
        ident(this);
        ready();
        setup((PARSpec) c_setup.read());
    }

    private void loop() {
        while (true) {
            idle(this);
            insert((IDSpec) c_insert.read());
            pin((PINSpec) c_pin.read());
            Boolean authorized = auth(theID, thePin);
            Logger.global.finest("ATM gets auth answer:" + authorized.toString());
            if (authorized.booleanValue()) {
                amount((SUMSpec) c_amount.read());
                debit(this, theID, theAmount);
                result((Boolean) c_result.read());
                if (!returnCard.booleanValue()) {
                    money(this, theAmount);
                }
            }
            eject(this);
        }
    }

    public TellerMachine(ChannelOutput ready, ChannelOutput debit, Channel ident, Channel auth, AltingChannelInput setup, AltingChannelInput result, AltingChannelInput insert, AltingChannelInput pin, AltingChannelInput amount, ChannelOutput idle, ChannelOutput money, ChannelOutput eject) {
        this.c_ready = ready;
        this.c_debit = debit;
        this.c_ident = ident;
        this.c_auth = auth;
        this.c_setup = setup;
        this.c_result = result;
        this.c_insert = insert;
        this.c_pin = pin;
        this.c_amount = amount;
        this.c_idle = idle;
        this.c_money = money;
        this.c_eject = eject;
        main = new CSProcess() {

            public void run() {
                init();
                loop();
            }
        };
    }
}
