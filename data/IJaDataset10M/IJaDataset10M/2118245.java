package pensjonsberegning;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import pensjonsberegning.bdoc.Ref;
import pensjonsberegning.bdoc.RefClass;
import pensjonsberegning.bdoc.Story;

/**
 * @author Per Otto Bergum Christensen
 */
@Ref(Story.BEREGNING_AV_ALDERSPENSJON)
@RefClass(Folketrygdberegningtjeneste.class)
public class TestFolketrygdberegningtjenesteBehaviour extends ScenarioSupport<TestFolketrygdberegningtjenesteBehaviour> {

    private GrunnbeloepTabell grunnbeloepTabell = new GrunnbeloepTabell();

    private Folketrygdberegningtjeneste folketrygdberegningTjeneste = new Folketrygdberegningtjeneste(grunnbeloepTabell);

    private MedlemAvFolketrygden person;

    private Alderspensjon alderspensjon;

    @Test
    public void skal_beregne_alderspensjon_for_et_medlem_av_folketrygden() {
        gitt.et_medlem_av_folketrygden_med_silvitstatus_enslig();
        og.inntekt_lik(_40_aar_med_maksimal_opptjening());
        naar.alderspensjon_blir_beregnet_for_aaret(2009);
        saa.skal_aarlig_alderspensjon_vaere_lik(333823);
        hvor.tilleggspensjonen_utgjoer(261817);
        og.grunnpensjon_utgjoer(Grunnbeloep._2009);
        samtidig.skal_sluttpoengtallet_vaere_beregnet_til(8.33);
        og.pensjonsprosenten_skal_vaere_beregnet_til(0.4365);
    }

    void inntekt_lik(List<Inntekt> inntekt) {
        person.setInntekt(inntekt);
    }

    List<Inntekt> _40_aar_med_maksimal_opptjening() {
        List<Inntekt> inntekt = new ArrayList<Inntekt>();
        for (int aar = 1970; aar < 2010; aar++) {
            inntekt.add(new Inntekt(aar, (grunnbeloepTabell.gjennomsnittligGrunnbeloepFor(aar) * 12)));
        }
        return inntekt;
    }

    void gittInntektLik(List<Inntekt> inntekt) {
        person.setInntekt(inntekt);
    }

    void grunnpensjon_utgjoer(double grunnpensjon) {
        assertEquals(grunnpensjon, alderspensjon.getGrunnpensjon(), .001);
    }

    void pensjonsprosenten_skal_vaere_beregnet_til(double pensjonsprosent) {
        assertEquals(pensjonsprosent, alderspensjon.getTilleggspensjon().getPensjonsprosent(), .0001);
    }

    void skal_sluttpoengtallet_vaere_beregnet_til(double sluttpoengtall) {
        assertEquals(sluttpoengtall, alderspensjon.getTilleggspensjon().getSluttpoengtall(), .001);
    }

    void skal_aarlig_alderspensjon_vaere_lik(int aarligAlderspensjon) {
        assertEquals(aarligAlderspensjon, alderspensjon.beregnet(), .1);
    }

    void et_medlem_av_folketrygden_med_silvitstatus_enslig() {
        person = new MedlemAvFolketrygden();
    }

    void alderspensjon_blir_beregnet_for_aaret(int aar) {
        alderspensjon = folketrygdberegningTjeneste.beregnAlderspensjonFor(person, aar);
    }

    void tilleggspensjonen_utgjoer(int aarligAlderspensjon) {
        assertEquals(aarligAlderspensjon, alderspensjon.getTilleggspensjon().beregnet(), .1);
    }
}
