package sratworld.base.actor.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActorLineParserImpl implements ActorLineParser {

    public ActorLine parse(String theLine, int lineNumber) throws MalformedActorLineException {
        ActorLine parsedLine = new ActorLine();
        parsedLine.setOriginalLine(theLine);
        parsedLine.setOriginalLinenumber(lineNumber);
        try {
            theLine = theLine.trim();
            String regex;
            Matcher m;
            if (theLine.length() == 0 || theLine.charAt(0) == '%') {
                return null;
            }
            regex = "^SRaT-Actor:\\s(01)$";
            m = Pattern.compile(regex).matcher(theLine);
            if (m.matches()) {
                parsedLine.setActorLineType(ActorLineType.VersionInfo);
                int version = Integer.parseInt(m.group(1));
                parsedLine.getTokens().add(version);
                return parsedLine;
            }
            regex = "^\\(([^,]+,\\s?)*[^,]+?\\)$";
            m = Pattern.compile(regex).matcher(theLine);
            if (m.matches()) {
                parsedLine.setActorLineType(ActorLineType.Inventory);
                theLine = theLine.substring(1, theLine.length() - 1);
                String[] tokens = theLine.split(",");
                for (String token : tokens) {
                    token = token.trim();
                    if (!quoteWhiteSpaceIsConsistent(token)) {
                        throw new MalformedActorLineException("", parsedLine.getOriginalLinenumber(), parsedLine.getOriginalLine());
                    }
                    token = trimQuotes(token);
                    parsedLine.getTokens().add(token);
                }
                return parsedLine;
            }
            regex = "^([\\S-]+)\\s=\\s(.+)$";
            m = Pattern.compile(regex).matcher(theLine);
            if (m.matches()) {
                if (!quoteWhiteSpaceIsConsistent(m.group(2))) {
                    throw new MalformedActorLineException("", parsedLine.getOriginalLinenumber(), parsedLine.getOriginalLine());
                }
                String attributeLocationOrWear = m.group(1);
                parsedLine.getTokens().add(attributeLocationOrWear);
                if (attributeLocationOrWear.equals("name")) {
                    parsedLine.setActorLineType(ActorLineType.Name);
                    parsedLine.getTokens().add(trimQuotes(m.group(2)));
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("hero")) {
                    parsedLine.setActorLineType(ActorLineType.Hero);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(ActorLineType.Strength, (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("strength")) {
                    parsedLine.setActorLineType(ActorLineType.Strength);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("dexterity")) {
                    parsedLine.setActorLineType(ActorLineType.Dexterity);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("life")) {
                    parsedLine.setActorLineType(ActorLineType.Life);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("class-bonus")) {
                    parsedLine.setActorLineType(ActorLineType.ClassBonus);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("level")) {
                    parsedLine.setActorLineType(ActorLineType.Level);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("experience")) {
                    parsedLine.setActorLineType(ActorLineType.Experience);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("gold")) {
                    parsedLine.setActorLineType(ActorLineType.Gold);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("area")) {
                    parsedLine.setActorLineType(ActorLineType.LocationArea);
                    parsedLine.getTokens().add(trimQuotes(m.group(2)));
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("row")) {
                    parsedLine.setActorLineType(ActorLineType.LocationRow);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("col")) {
                    parsedLine.setActorLineType(ActorLineType.LocationCol);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("create-pct")) {
                    parsedLine.setActorLineType(ActorLineType.LocationCreatePct);
                    parsedLine.getTokens().add(Integer.parseInt(m.group(2)));
                    verifyBoundary(parsedLine.getActorLineType(), (Integer) parsedLine.getTokens().get(1), parsedLine);
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("weapon-hand")) {
                    parsedLine.setActorLineType(ActorLineType.WearWeaponHand);
                    parsedLine.getTokens().add(trimQuotes(m.group(2)));
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("shield-hand")) {
                    parsedLine.setActorLineType(ActorLineType.WearShieldHand);
                    parsedLine.getTokens().add(trimQuotes(m.group(2)));
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("helmet")) {
                    parsedLine.setActorLineType(ActorLineType.WearHelmet);
                    parsedLine.getTokens().add(trimQuotes(m.group(2)));
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("torso")) {
                    parsedLine.setActorLineType(ActorLineType.WearTorso);
                    parsedLine.getTokens().add(trimQuotes(m.group(2)));
                    return parsedLine;
                } else if (attributeLocationOrWear.equals("legs")) {
                    parsedLine.setActorLineType(ActorLineType.WearLegs);
                    parsedLine.getTokens().add(trimQuotes(m.group(2)));
                    return parsedLine;
                }
            }
            if (theLine.charAt(0) == '[' && theLine.charAt(theLine.length() - 1) == ']') {
                String lineContent = theLine.substring(1, theLine.length() - 1);
                if (lineContent.equals("Actor")) {
                    parsedLine.setActorLineType(ActorLineType.ActorSection);
                    return parsedLine;
                } else if (lineContent.equals("Actor:wear")) {
                    parsedLine.setActorLineType(ActorLineType.WearSection);
                    return parsedLine;
                } else if (lineContent.equals("Actor:inventory")) {
                    parsedLine.setActorLineType(ActorLineType.InventorySection);
                    return parsedLine;
                } else if (lineContent.equals("Actor:location")) {
                    parsedLine.setActorLineType(ActorLineType.LocationSection);
                    return parsedLine;
                } else {
                    throw new MalformedActorLineException("", parsedLine.getOriginalLinenumber(), parsedLine.getOriginalLine());
                }
            }
        } catch (Exception ex) {
            throw new MalformedActorLineException("", parsedLine.getOriginalLinenumber(), parsedLine.getOriginalLine());
        }
        throw new MalformedActorLineException("", parsedLine.getOriginalLinenumber(), parsedLine.getOriginalLine());
    }

    private static String trimQuotes(String unquotedToken) {
        if (unquotedToken.charAt(0) == '"' && unquotedToken.charAt(unquotedToken.length() - 1) == '"') {
            unquotedToken = unquotedToken.substring(1, unquotedToken.length() - 1);
        }
        return unquotedToken;
    }

    private static boolean quoteWhiteSpaceIsConsistent(String unquotedToken) {
        if (unquotedToken.charAt(0) == '"' && unquotedToken.charAt(unquotedToken.length() - 1) == '"') {
            if (!unquotedToken.contains(" ")) {
                return false;
            }
        } else {
            if (unquotedToken.contains(" ")) {
                return false;
            }
        }
        return true;
    }

    private static void verifyBoundary(ActorLineType linetype, Integer i, ActorLine parsedLine) throws MalformedActorLineException {
        switch(linetype) {
            case Strength:
            case Gold:
                if (i < 0 || i > 999) {
                    throw new MalformedActorLineException("", parsedLine.getOriginalLinenumber(), parsedLine.getOriginalLine());
                }
            default:
                break;
        }
    }
}
