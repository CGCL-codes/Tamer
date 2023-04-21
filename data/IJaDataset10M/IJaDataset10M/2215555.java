package uk.ac.ebi.rhea.ws.core.mapper;

import java.math.BigInteger;
import java.util.EnumMap;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Direction;
import uk.ac.ebi.rhea.find.ReactionFinder;
import uk.ac.ebi.rhea.ws.core.util.RheaCoreUtil;
import uk.ac.ebi.rhea.ws.response.cmlreact.Molecule;

/**
 * This class contains methods that can be invoked to map the Rhea data types to
 * CmlReact data types.
 *
 * @author <a href="mailto:hongcao@ebi.ac.uk">Hong Cao</a>
 * @since 02-07-2010
 */
public class CmlMapper extends uk.ac.ebi.rhea.ws.response.cmlreact.Reaction {

    public CmlMapper(uk.ac.ebi.rhea.find.ReactionFinder reactionFinder) {
        this.reactionFinder = reactionFinder;
    }

    private static Logger logger = Logger.getLogger(CmlMapper.class);

    private uk.ac.ebi.rhea.find.ReactionFinder reactionFinder;

    public ReactionFinder getReactionFinder() {
        return reactionFinder;
    }

    public void setReactionFinder(ReactionFinder reactionFinder) {
        this.reactionFinder = reactionFinder;
    }

    public uk.ac.ebi.rhea.ws.response.cmlreact.Reaction mapRheaReaction() {
        uk.ac.ebi.rhea.domain.Reaction reaction = reactionFinder.getReaction();
        String rheaIdString = String.valueOf(reaction.getId());
        this.setId(RheaCoreUtil.createRheaIdWithColon(rheaIdString));
        String RheaTitle = RheaCoreUtil.createRheaIdWithColon(rheaIdString);
        this.setTitle(RheaTitle);
        Direction direction = reaction.getDirection();
        String directionConv = RheaCoreUtil.createRheaDirectionConvention(direction.getCode());
        this.setConvention(directionConv);
        List rheaLeftSideList = (List) reaction.getLeftSide();
        List rheaRightSideList = (List) reaction.getRightSide();
        if (direction.getCode().equalsIgnoreCase(Direction.RL.getCode())) {
            mapReactionDirection(rheaRightSideList, rheaLeftSideList);
        } else {
            mapReactionDirection(rheaLeftSideList, rheaRightSideList);
        }
        CmlLinkMapMapper cmlLinkMapMapper = new CmlLinkMapMapper(reaction);
        if (reaction.getFamilyXrefs() != null) {
            this.getReactiveCentreAndMechanismAndReactantList().add(cmlLinkMapMapper.mapXrefs());
        }
        EnumMap relatedRhea = this.reactionFinder.getRelatedReactions();
        if (relatedRhea != null) {
            this.getReactiveCentreAndMechanismAndReactantList().add(cmlLinkMapMapper.mapRelatedReactionListToMap(relatedRhea));
        }
        this.id = reaction.getId().toString();
        return this;
    }

    public void mapReactionDirection(List leftList, List rightList) {
        CmlReactantListMapper cmlReactantListMapper = new CmlReactantListMapper();
        this.getReactiveCentreAndMechanismAndReactantList().add(cmlReactantListMapper.mapReactantList(leftList));
        CmlProducListMapper cmlProducListMapper = new CmlProducListMapper();
        this.getReactiveCentreAndMechanismAndReactantList().add(cmlProducListMapper.mapProductList(rightList));
    }

    static Molecule mapRheaCompound(Compound compound) {
        Molecule molecule = new Molecule();
        molecule.setTitle(RheaCoreUtil.xCharToString(compound.getName()));
        molecule.setFormula(compound.getFormula());
        molecule.setFormalCharge(BigInteger.valueOf(compound.getCharge()));
        molecule.setId(compound.getAccession());
        return molecule;
    }
}
