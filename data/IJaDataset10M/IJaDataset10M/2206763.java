package objects.util;

import java.util.*;
import java.util.regex.Pattern;

public final class GamerAddress {

    private static final String STR_ALL = "ALL";

    private static final Pattern pattern = Pattern.compile(":");

    private static final Random random = new Random();

    private final javax.mail.internet.InternetAddress address;

    private final EnumSet<Mode> modes;

    private String confirm;

    public GamerAddress(javax.mail.internet.InternetAddress address, Set<Mode> modes, String confirm) {
        this.address = address;
        this.modes = EnumSet.copyOf(modes);
        this.confirm = confirm;
    }

    public static GamerAddress parse(String str, boolean full) throws GamerAddressException {
        String[] strings = pattern.split(str);
        if (strings.length == 0) throw new GamerAddressException("Empty e-mail address");
        javax.mail.internet.InternetAddress address;
        try {
            address = new javax.mail.internet.InternetAddress(strings[0].toLowerCase());
            address.validate();
        } catch (javax.mail.internet.AddressException err) {
            throw new GamerAddressException("Illegal e-mail address: {0}", strings[0].toLowerCase());
        }
        EnumSet<Mode> modes = EnumSet.noneOf(Mode.class);
        String code = null;
        Loop: for (int i = 1; i < strings.length; ++i) {
            String suffix = strings[i];
            if (suffix.equals(STR_ALL)) {
                modes.addAll(EnumSet.allOf(Mode.class));
                continue;
            }
            for (Mode mode : Mode.values()) if (suffix.equals(mode.str)) {
                modes.add(mode);
                continue Loop;
            }
            if (full && suffix.startsWith("=")) code = suffix.substring(1); else throw new GamerAddressException("Illegal address suffix: {0}", suffix);
        }
        if (modes.isEmpty()) modes.addAll(EnumSet.allOf(Mode.class));
        return new GamerAddress(address, modes, code);
    }

    final Set<Mode> getModes() {
        return modes.clone();
    }

    final void addModes(Set<Mode> modes) {
        this.modes.addAll(modes);
    }

    final void removeModes(Set<Mode> modes) {
        this.modes.removeAll(modes);
    }

    public final String[] getModeStrings() {
        List<String> type = new ArrayList<String>();
        for (Mode mode : getModes()) type.add(mode.name().toLowerCase());
        return type.toArray(new String[type.size()]);
    }

    public final void setConfirmCode(String code) {
        confirm = code;
    }

    public final void generateConfirmCode() {
        confirm = Integer.toString(random.nextInt(Integer.MAX_VALUE), Character.MAX_RADIX);
    }

    public final void confirm() {
        confirm = null;
    }

    public final String getConfirmCode() {
        return confirm;
    }

    public final boolean isConfirmed() {
        return confirm == null;
    }

    final boolean canReceive(Mode mod) {
        return modes.contains(mod);
    }

    public final boolean canReceiveAll() {
        return modes.containsAll(EnumSet.allOf(Mode.class));
    }

    public final String getString() {
        return address.getAddress();
    }

    public final javax.mail.internet.InternetAddress getAddress() {
        return address;
    }

    @Override
    public final boolean equals(Object object) {
        return object instanceof GamerAddress && equals((GamerAddress) object);
    }

    public final boolean equals(GamerAddress addr) {
        return addr.getString().equals(getString());
    }

    public final String toString(boolean full) {
        StringBuilder buffer = new StringBuilder(address.getAddress());
        if (!canReceiveAll()) {
            for (Mode mode : modes) buffer.append(':').append(mode.str);
        }
        if (confirm != null) {
            if (full) buffer.append(":=").append(confirm); else buffer.append(":(unconfirmed)");
        }
        return buffer.toString();
    }

    public enum Mode {

        MAIL("MAIL"), REPORT("REP"), ANSWER("ANS");

        private final String str;

        Mode(String str) {
            this.str = str;
        }
    }
}
