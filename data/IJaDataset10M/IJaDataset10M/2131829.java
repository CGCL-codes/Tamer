package br.net.woodstock.rockframework.security.util;

import br.net.woodstock.rockframework.security.digest.DigestType;
import br.net.woodstock.rockframework.security.digest.Digester;
import br.net.woodstock.rockframework.security.digest.impl.AsStringDigester;
import br.net.woodstock.rockframework.security.digest.impl.Base64Digester;
import br.net.woodstock.rockframework.security.digest.impl.BasicDigester;
import br.net.woodstock.rockframework.util.Assert;

public class DigesterPasswordEncoder implements PasswordEncoder {

    private AsStringDigester digester;

    public DigesterPasswordEncoder(final DigestType type) {
        super();
        Assert.notNull(type, "type");
        Digester basic = new BasicDigester(type);
        Digester base64 = new Base64Digester(basic);
        this.digester = new AsStringDigester(base64);
    }

    @Override
    public String encode(final String password) {
        return this.digester.digestAsString(password);
    }
}
