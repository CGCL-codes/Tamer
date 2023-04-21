package com.cbsgmbh.xi.af.edifact.crypt.worker.iaikbc;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import com.cbsgmbh.xi.af.edifact.crypt.worker.Decrypter;
import com.cbsgmbh.xi.af.edifact.crypt.worker.Encrypter;
import com.cbsgmbh.xi.af.edifact.crypt.worker.Signer;
import com.cbsgmbh.xi.af.edifact.crypt.worker.SignerMdn;
import com.cbsgmbh.xi.af.edifact.crypt.worker.Verifier;
import com.cbsgmbh.xi.af.edifact.crypt.worker.WorkerCreator;
import com.cbsgmbh.xi.af.edifact.module.crypt.configuration.ConfigurationSettings;

public class WorkerCreatorIaikBc implements WorkerCreator {

    public Decrypter createDecrypter(MimeBodyPart part, X509Certificate receiverCert, PrivateKey receiverPrivateKey, ConfigurationSettings configuration) {
        return new DecrypterImpl(part, receiverCert, receiverPrivateKey, configuration);
    }

    public Encrypter createEncrypter(MimeBodyPart part, X509Certificate receiverCert, String algorithmSym) {
        return new EncrypterImpl(part, receiverCert, algorithmSym);
    }

    public Signer createSigner(MimeBodyPart part, X509Certificate senderCert, PrivateKey senderPrivateKey, String micAlg) {
        return new SignerImpl(part, senderCert, senderPrivateKey, micAlg);
    }

    public SignerMdn createSignerMdn(MimeMultipart part, X509Certificate senderCert, PrivateKey senderPrivateKey, String micAlg) {
        return new SignerMdnImpl(part, senderCert, senderPrivateKey, micAlg);
    }

    public Verifier createVerifier(MimeBodyPart part, X509Certificate senderCert) {
        return new VerifierImpl(part, senderCert);
    }
}
