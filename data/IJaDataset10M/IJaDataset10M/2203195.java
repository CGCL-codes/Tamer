package net.sourceforge.freejava.type.info;

import junit.framework.TestCase;
import net.sourceforge.freejava.lang.FinalNegotiation;
import net.sourceforge.freejava.lang.INegotiation;
import net.sourceforge.freejava.lang.NegotiationException;
import net.sourceforge.freejava.lang.NegotiationParameter;
import net.sourceforge.freejava.lang.UnsupportedNegotiationException;
import net.sourceforge.freejava.type.AbstractTypeTraits;
import net.sourceforge.freejava.type.ITypeTraits;
import net.sourceforge.freejava.type.TypeTraitsResolve;
import net.sourceforge.freejava.type.traits.IParser;
import net.sourceforge.freejava.util.exception.ParseException;
import org.junit.Test;

public class TypeTraitsDemoTest extends TestCase {

    static class CountryAlias {

        public String unalias(String alias) {
            if ("cn".equals(alias)) return "China";
            return alias;
        }
    }

    static class PostCode {

        public String getCity(String code) {
            if (code.equals("310000")) return "Zhejiang";
            throw new IllegalArgumentException("Invalid post code: " + code);
        }
    }

    static class Address {

        String country;

        String city;

        String location;

        public Address(String country, String city, String location) {
            this.country = country;
            this.city = city;
            this.location = location;
        }

        @Override
        public String toString() {
            return country + ":" + city + ":" + location;
        }

        private static AddressTraits traits = new AddressTraits(true);

        static AddressTraits getTypeTraits() {
            return traits;
        }
    }

    static class AddressTraits extends AbstractTypeTraits<Address> {

        private final boolean resolvePostCode;

        public AddressTraits(boolean resolvePostCode) {
            super(Address.class);
            this.resolvePostCode = resolvePostCode;
        }

        @Override
        public Address parse(String text) throws ParseException {
            try {
                return parse(text, null);
            } catch (NegotiationException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        @Override
        public Address parse(String text, INegotiation negotiation) throws ParseException, NegotiationException {
            String[] segs = text.split(":", 3);
            if (segs.length < 3) throw new ParseException("Address format=COUNTRY:POSTCODE:ADDRESS");
            if (negotiation != null) {
                for (NegotiationParameter param : negotiation) {
                    if (param.isWanted(CountryAlias.class)) segs[0] = param.getValue(CountryAlias.class).unalias(segs[0]); else if (param.isWanted(PostCode.class, resolvePostCode)) segs[1] = param.getValue(PostCode.class).getCity(segs[1]); else throw new UnsupportedNegotiationException(param);
                }
            }
            return new Address(segs[0], segs[1], segs[2]);
        }
    }

    static class PostAddressTypeInfo extends AddressTraits {

        public PostAddressTypeInfo() {
            super(true);
        }
    }

    static ITypeTraits<? super Address> postAddressType = TypeTraitsResolve.findTraits(Address.class);

    static ITypeTraits<? super Address> nonpostAddressType = new AddressTraits(false);

    @Test
    public void testDefaultNoChange() throws Exception {
        postAddressType.getSampleGenerator().newSample();
        Address address = (Address) postAddressType.query(IParser.class).parse("cn:310000:somewhere");
        assertEquals("cn:310000:somewhere", address.toString());
    }

    @Test
    public void testDefaultCountryPost() throws Exception {
        FinalNegotiation negotiation = new FinalNegotiation(new NegotiationParameter(new CountryAlias()), new NegotiationParameter(new PostCode()));
        Address address = (Address) postAddressType.query(IParser.class).parse("cn:310000:somewhere", negotiation);
        assertEquals("China:Zhejiang:somewhere", address.toString());
    }

    @Test(expected = UnsupportedNegotiationException.class)
    public void testMandatoryFail() throws Exception {
        FinalNegotiation negotiation = new FinalNegotiation(new NegotiationParameter(new CountryAlias()), new NegotiationParameter(new PostCode(), true));
        nonpostAddressType.query(IParser.class).parse("cn:310000:somewhere", negotiation);
    }

    @Test(expected = UnsupportedNegotiationException.class)
    public void testPostIgnored() throws Exception {
        FinalNegotiation negotiation = new FinalNegotiation(new NegotiationParameter(new CountryAlias()), new NegotiationParameter(new PostCode()));
        Address address = (Address) nonpostAddressType.query(IParser.class).parse("cn:310000:somewhere", negotiation);
        assertEquals("China:3100:somewhere", address.toString());
    }
}
